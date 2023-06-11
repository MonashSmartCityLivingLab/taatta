package edu.monash.humanise.smartcity.collector

import io.github.oshai.KotlinLogging
import org.json.JSONException
import org.json.JSONObject
import org.springframework.messaging.Message
import org.springframework.web.client.RestClientException


private val logger = KotlinLogging.logger {}

class Router(private val message: Message<*>) {
    private val isDocker = System.getenv("TAATTA_DOCKER") == "1"
    private val loraModules = arrayOf(
            SensorModule("rhf1s001", 4040, if (isDocker) {
                "rhf1s001"
            } else {
                "localhost"
            }),
            SensorModule("wqm101", 4042, if (isDocker) {
                "wqm101"
            } else {
                "localhost"
            }),
            SensorModule("df702", 4044, if (isDocker) {
                "df702"
            } else {
                "localhost"
            }),
            SensorModule("tbs220", 4046, if (isDocker) {
                "tbs220"
            } else {
                "localhost"
            }),
            SensorModule("pcr2", 4048, if (isDocker) {
                "pcr2"
            } else {
                "localhost"
            }),
    )
    private val athomModule = SensorModule("athom-smart-plug", 4050, if (isDocker) {
        "athom-smart-plug"
    } else {
        "localhost"
    })

    fun route() {
        val payload = message.headers.toString()
        val topic: String = message.headers["mqtt_receivedTopic"] as String
        logger.info { "New message. Topic: $topic, payload: $payload" }

        when {
            topic.startsWith("athom-smart-plug") -> {
                val topicComponents = topic.split("/")
                try {
                    val jsonObject = JSONObject()
                    jsonObject.put("deviceName", topicComponents[0])
                    jsonObject.put("data", payload)
                    jsonObject.put("sensor", topicComponents[2])
                    val response = athomModule.sendData(jsonObject)
                    logger.info { "Response from athom-smart-plug is: $response" }
                } catch (e: RestClientException) {
                    logger.error(e) { "Cannot send data to athom-smart-plug module" }
                } catch (e: JSONException) {
                    logger.error(e) { "Cannot create JSON object for smart plug" }
                    throw RuntimeException(e)
                }
            }

            // for chirpstack
            else -> {
                try {
                    val jsonObject = JSONObject(payload)
                    if (jsonObject.has("data")) {
                        val deviceProfile = jsonObject.getString("deviceProfileName")
                        val sensors = loraModules.filter { sensorModule -> deviceProfile.endsWith(sensorModule.sensorName) }
                        try {
                            val response = sensors[0].sendData(jsonObject)
                            logger.info { "Response from $deviceProfile is: $response" }
                        } catch (e: RestClientException) {
                            logger.error(e) { "Cannot send data to $deviceProfile module" }
                        } catch (_: IndexOutOfBoundsException) {
                            logger.error { "This device type $deviceProfile not implemented yet." }
                        }
                    } else {
                        logger.warn { "No data field found. Skipping" }
                    }
                } catch (e: JSONException) {
                    logger.error(e) { "Cannot parse JSON" }
                    throw RuntimeException(e)
                }
            }
        }
    }
}