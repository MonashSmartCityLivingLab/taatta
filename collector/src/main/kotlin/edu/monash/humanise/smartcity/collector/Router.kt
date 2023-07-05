package edu.monash.humanise.smartcity.collector

import edu.monash.humanise.smartcity.collector.payload.EspHomePayload
import edu.monash.humanise.smartcity.collector.payload.LoRaPayload
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.messaging.Message
import java.io.File
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

/**
 * Routes the message to the appropriate logger modules.
 */
class Router {
    companion object {
        /** Sensor config */
        private val sensorRoutersConfig: SensorRoutersConfig

        /**
         * JSON encoder/decoder for router.
         */
        // only decode what's needed and ignore any unknown keys
        private val jsonCoder = Json { ignoreUnknownKeys = true }

        init {
            val configPath = System.getenv("TAATTA_SENSOR_ROUTERS") ?: "sensorRouters.json"
            try {
                val configFile = File(configPath)
                logger.info { "Reading sensor router config from ${configFile.absolutePath}" }
                val configJson = configFile.readText()
                sensorRoutersConfig = jsonCoder.decodeFromString(configJson)
                logger.info { "Loaded ESPHome routers: ${sensorRoutersConfig.espHomeModules.map { s -> s.name }}" }
                logger.info { "Loaded LoRa routers: ${sensorRoutersConfig.loraModules.map { s -> s.name }}" }
            } catch (e: Exception) {
                logger.error(e) { "Cannot open module config at $configPath" }
                throw RuntimeException(e)
                exitProcess(1)
            }
        }

        /**
         * Routes a message to the appropriate logger modules.
         *
         * @param message A [Message] instance containing MQTT message
         */
        fun route(message: Message<*>) {
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
                        val jsonObject = EspHomePayload(deviceName, payloadSensor, payload)
                        val results = matchingEspHomeSensor.sendData(jsonCoder.encodeToString(jsonObject))
                        results.forEach { result ->
                            result.onSuccess { response -> logger.debug { "Response from ${matchingEspHomeSensor.name} is: $response" } }
                            result.onFailure { error -> logger.error(error) { "Cannot send data for sensor ${matchingEspHomeSensor.name}" } }
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
                    val jsonObject: LoRaPayload = jsonCoder.decodeFromString(payload)
                    if (jsonObject.data != null) {
                        val deviceProfile = jsonObject.deviceProfileName
                        val sensor = sensorRoutersConfig.loraModules.firstOrNull { sensorModule ->
                            deviceProfile.endsWith(sensorModule.name)
                        }
                        if (sensor != null) {
                            val results = sensor.sendData(jsonCoder.encodeToString(jsonObject))
                            results.forEach { result ->
                                result.onSuccess { response -> logger.debug { "Response from ${sensor.name} is: $response" } }
                                result.onFailure { error -> logger.error(error) { "Cannot send data for sensor ${sensor.name}" } }
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
    }
}