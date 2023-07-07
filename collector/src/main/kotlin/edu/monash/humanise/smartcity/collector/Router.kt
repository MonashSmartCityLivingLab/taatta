package edu.monash.humanise.smartcity.collector

import edu.monash.humanise.smartcity.collector.payload.ChirpStackUplinkEvent
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
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

/**
 * Component which routes the message to the appropriate logger modules.
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

    /**
     * Routes a message to the appropriate logger modules.
     *
     * @param message A [Message] instance containing MQTT message
     */
    fun route(message: Message<*>) {
        val timestamp = message.headers.timestamp ?: Instant.now().toEpochMilli()
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
                    val results = matchingEspHomeSensor.sendData(jsonCoder.encodeToString(jsonObject))
                    results.forEach { result ->
                        result.onSuccess { response -> logger.debug { "Response from ${matchingEspHomeSensor.name} is: $response" } }
                        result.onFailure { error -> logger.debug { "Cannot send data for sensor ${matchingEspHomeSensor.name}: $error" } }
                    }
                } catch (e: SerializationException) {
                    logger.error(e) { "Cannot parse JSON for topic $topic" }
                }
            } else {
                logger.warn { "This device type $deviceName not implemented yet, skipping." }
            }

        } else {
            // handle lora sensors
            try {
                val event: ChirpStackUplinkEvent = jsonCoder.decodeFromString(payload)
                if (event.data != null) {
                    val deviceProfile = event.deviceProfileName
                    val sensor = sensorRoutersConfig.loraModules.firstOrNull { sensorModule ->
                        deviceProfile.endsWith(sensorModule.name)
                    }
                    if (sensor != null) {
                        val jsonObject = LoRaPayload(timestamp, event.deviceName, event.devEUI, event.data)
                        val results = sensor.sendData(jsonCoder.encodeToString(jsonObject))
                        results.forEach { result ->
                            result.onSuccess { response -> logger.debug { "Response from ${sensor.name} is: $response" } }
                            result.onFailure { error -> logger.debug { "Cannot send data for sensor ${sensor.name}: $error" } }
                        }
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

    /**
     * Retry any failed requests every minute.
     */
    @Scheduled(cron = "0 * * * * *")
    fun retrySendingData() {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        logger.info { "Retrying any failed requests from $previousTime to $now" }
        var successCount = 0
        var failCount = 0
        sensorRoutersConfig.espHomeModules.forEach { router ->
            val results = router.retryFailedRequests()
            results.forEach { result ->
                result.onSuccess { response ->
                    logger.debug { "Response from ${router.name} is: $response" }
                    successCount += 1
                }
                result.onFailure { error ->
                    logger.error(error) { "Cannot send data for sensor ${router.name} after a retry" }
                    failCount += 1
                }
            }
        }
        sensorRoutersConfig.loraModules.forEach { router ->
            val results = router.retryFailedRequests()
            results.forEach { result ->
                result.onSuccess { response ->
                    logger.debug { "Response from ${router.name} is: $response" }
                    successCount += 1
                }
                result.onFailure { error ->
                    logger.error(error) { "Cannot send data for sensor ${router.name} after a retry" }
                    failCount += 1
                }
            }
        }

        logger.info { "Number of retried requests: $successCount succeeded, $failCount failed"}
        previousTime = now
    }
}
