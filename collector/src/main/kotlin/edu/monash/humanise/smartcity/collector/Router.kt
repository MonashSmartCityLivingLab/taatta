package edu.monash.humanise.smartcity.collector

import edu.monash.humanise.smartcity.collector.payload.EspHomePayload
import edu.monash.humanise.smartcity.collector.payload.LoRaPayload
import io.github.oshai.KotlinLogging
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

        init {
            val configFile = System.getenv("TAATTA_SENSOR_ROUTERS") ?: "sensorRouters.json"
            try {
                logger.info { "Reading sensor module config from $configFile" }
                val configJson = File(configFile).readText()
                sensorRoutersConfig = Json.decodeFromString(configJson)
                logger.info { "Loaded ESPHome sensors: ${sensorRoutersConfig.espHomeModules.map { s -> s.name }}" }
                logger.info { "Loaded LoRa sensors: ${sensorRoutersConfig.loraModules.map { s -> s.name }}" }
            } catch (e: Exception) {
                logger.error(e) { "Cannot open module config at $configFile" }
                throw RuntimeException(e)
                exitProcess(1)
            }
        }

        fun route(message: Message<*>) {
            val payload = message.payload.toString()
            val topic: String = message.headers["mqtt_receivedTopic"] as String
            logger.debug { "New message. Topic: $topic, payload: $payload" }

            if (!topic.startsWith("application")) {
                val topicComponents = topic.split("/")
                val deviceName = topicComponents[0]
                // try matching esphome sensors
                val matchingEspHomeSensor = sensorRoutersConfig.espHomeModules.firstOrNull { sensor -> topic.startsWith(sensor.name) }
                if (matchingEspHomeSensor != null) {
                    try {

                        val payloadSensor = when (topicComponents[1]) {
                            "status" -> "status"
                            else -> topicComponents[2]
                        }
                        val jsonObject = EspHomePayload(deviceName, payloadSensor, payload)
                        val results = matchingEspHomeSensor.sendData(Json.encodeToString(jsonObject))
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
                    val jsonObject: LoRaPayload = Json.decodeFromString(payload)
                    if (jsonObject.data != null) {
                        val deviceProfile = jsonObject.deviceProfileName
                        val sensor = sensorRoutersConfig.loraModules.firstOrNull { sensorModule -> deviceProfile.endsWith(sensorModule.name) }
                        if (sensor != null) {
                            val results = sensor.sendData(Json.encodeToString(jsonObject))
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