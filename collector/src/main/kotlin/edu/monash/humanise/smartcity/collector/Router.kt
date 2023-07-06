package edu.monash.humanise.smartcity.collector

import edu.monash.humanise.smartcity.collector.payload.ChirpStackEvent
import edu.monash.humanise.smartcity.collector.payload.EspHomePayload
import edu.monash.humanise.smartcity.collector.payload.LoRaPayload
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

/**
 * Routes the message to the appropriate logger modules.
 */
@Component
class Router @Autowired constructor(configLoader: SensorRoutersConfigLoader) {
    /** Sensor config */
    private val sensorRoutersConfig: SensorRoutersConfig

    /**
     * JSON encoder/decoder for router.
     */
    // only decode what's needed and ignore any unknown keys
    private val jsonCoder = Json { ignoreUnknownKeys = true }

    // some internal state for logging purposes
    private var previousTime = OffsetDateTime.now(ZoneOffset.UTC)

    init {
        this.sensorRoutersConfig = configLoader.loadConfig()
    }

    fun route(message: Message<*>) {
        val timestamp = message.headers.timestamp ?: OffsetDateTime.now(ZoneOffset.UTC).toInstant().toEpochMilli()
        val payload = message.payload.toString()
        val topic: String = message.headers["mqtt_receivedTopic"] as String
        logger.debug { "New message. Topic: $topic, payload: $payload" }

        if (!topic.startsWith("application")) {
            val topicComponents = topic.split("/")
            val deviceName = topicComponents[0]
            // try matching esphome sensors
            val matchingEspHomeSensor =
                sensorRoutersConfig.espHomeModules.firstOrNull { sensor -> topic.startsWith(sensor.name) }
            if (matchingEspHomeSensor != null) {
                try {
                    val payloadSensor = when (topicComponents[1]) {
                        "status" -> "status"
                        else -> topicComponents[2]
                    }
                    val jsonObject = EspHomePayload(timestamp, deviceName, payloadSensor, payload)
                    matchingEspHomeSensor.addToQueue(jsonCoder.encodeToString(jsonObject))
                } catch (e: SerializationException) {
                    logger.error(e) { "Cannot parse JSON for topic $topic" }
                }
            } else {
                logger.warn { "This device type $deviceName not implemented yet, skipping." }
            }

        } else {
            // handle lora sensors
            try {
                val event: ChirpStackEvent = jsonCoder.decodeFromString(payload)
                if (event.data != null) {
                    val deviceProfile = event.deviceProfileName
                    val sensor = sensorRoutersConfig.loraModules.firstOrNull { sensorModule ->
                        deviceProfile.endsWith(sensorModule.name)
                    }
                    if (sensor != null) {
                        val jsonObject = LoRaPayload(timestamp, event.deviceName, event.devEUI, event.data)
                        sensor.addToQueue(jsonCoder.encodeToString(jsonObject))
                    } else {
                        logger.warn { "This device type $deviceProfile not implemented yet, skipping." }
                    }
                } else {
                    logger.warn { "Data field not found or null, skipping" }
                }
            } catch (e: SerializationException) {
                logger.error(e) { "Cannot parse JSON for topic $topic" }
            }
        }
    }

//    @Scheduled(cron = "0 * * * * *")
    @Scheduled(fixedDelay = 1000)
    fun sendData() {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        var successCount = 0
        var failCount = 0
        sensorRoutersConfig.espHomeModules.forEach { router ->
            val results = router.sendData()
            results.forEach { result ->
                result.onSuccess { response ->
                    logger.debug { "Response from ${router.name} is: $response" }
                    successCount += 1
                }
                result.onFailure { error ->
                    logger.error(error) { "Cannot send data for sensor ${router.name}" }
                    failCount += 1
                }
            }
        }
        sensorRoutersConfig.loraModules.forEach { router ->
            val results = router.sendData()
            results.forEach { result ->
                result.onSuccess { response ->
                    logger.debug { "Response from ${router.name} is: $response" }
                    successCount += 1
                }
                result.onFailure { error ->
                    logger.error(error) { "Cannot send data for sensor ${router.name}" }
                    failCount += 1
                }
            }
        }

        logger.info { "Number or requests from $previousTime to $now: $successCount succeeded, $failCount failed"}
        previousTime = now
    }
}
