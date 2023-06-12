package edu.monash.humanise.smartcity.collector

import io.github.oshai.KotlinLogging
import kotlinx.serialization.json.Json
import org.json.JSONException
import org.json.JSONObject
import org.springframework.messaging.Message
import java.io.File
import kotlin.system.exitProcess

private val logger = KotlinLogging.logger {}

class Router {
    companion object {
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
                        val jsonObject = JSONObject()
                        val payloadSensor = when (topicComponents[1]) {
                            "status" -> "status"
                            else -> topicComponents[2]
                        }
                        jsonObject.put("deviceName", deviceName)
                        jsonObject.put("data", payload)
                        jsonObject.put("sensor", payloadSensor)
                        val results = matchingEspHomeSensor.sendData(jsonObject)
                        results.forEach { result ->
                            result.onSuccess { response -> logger.debug { "Response from ${matchingEspHomeSensor.name} is: $response" } }
                            result.onFailure { error -> logger.error(error) { "Cannot send data for sensor ${matchingEspHomeSensor.name}" } }
                        }
                    } catch (e: JSONException) {
                        logger.error(e) { "Cannot parse JSON for topic $topic" }
                        throw RuntimeException(e)
                    }
                } else {
                    logger.warn { "This device type $deviceName not implemented yet, skipping." }
                }

            } else {
                // handle lora sensors
                try {
                    val jsonObject = JSONObject(payload)
                    if (jsonObject.has("data")) {
                        val deviceProfile = jsonObject.getString("deviceProfileName")
                        val sensor = sensorRoutersConfig.loraModules.firstOrNull { sensorModule -> deviceProfile.endsWith(sensorModule.name) }
                        if (sensor != null) {
                            val results = sensor.sendData(jsonObject)
                            results.forEach { result ->
                                result.onSuccess { response -> logger.debug { "Response from ${sensor.name} is: $response" } }
                                result.onFailure { error -> logger.error(error) { "Cannot send data for sensor ${sensor.name}" } }
                            }
                        } else {
                            logger.warn { "This device type $deviceProfile not implemented yet, skipping." }
                        }
                    } else {
                        logger.warn { "No data field found. Skipping" }
                    }
                } catch (e: JSONException) {
                    logger.error(e) { "Cannot parse JSON for topic $topic" }
                    throw RuntimeException(e)
                }
            }
        }
    }
}