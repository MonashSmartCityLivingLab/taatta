package edu.monash.humanise.smartcity.athompresencesensor

import edu.monash.humanise.smartcity.athompresencesensor.payload.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

/**
 * Service for storing presence sensor data.
 */
@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    @Value("\${smart-city.device-management.enabled}")
    private var deviceManagementEnabled: Boolean = false
    @Value("\${smart-city.device-management.url}")
    private lateinit var deviceManagementUrl: String

    /**
     * Decode the payload and save it as an appropriate entity in [Payload].
     */
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        val timestamp =
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(payloadRequest.timestampMilliseconds), ZoneOffset.UTC)
        val data = payloadRequest.data
        when (payloadRequest.sensor) {
            "pir_sensor" -> {
                val value = Decoder.decodeBinarySensorState(data)
                val payload = PirSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "mmwave_sensor" -> {
                val value = Decoder.decodeBinarySensorState(data)
                val payload = MmwaveSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "occupancy" -> {
                val value = Decoder.decodeBinarySensorState(data)
                val payload = OccupancyPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)

                // send data to management
                if (deviceManagementEnabled) {
                    val restTemplate = RestTemplate()
                    val url = "$deviceManagementUrl/occupancy"
                    val headers = initHttpHeaders()
                    // TODO: send data
                }
            }

            "uptime_sensor" -> {
                val value = Decoder.decodeLongSensorState(data)
                val payload = UptimePayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "wifi_signal_sensor" -> {
                val value = Decoder.decodeIntegerSensorState(data)
                val payload = WifiSignalPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "light_sensor" -> {
                val value = Decoder.decodeFloatSensorState(data)
                val payload = LightSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "status" -> {
                when (payloadRequest.data) {
                    "online" -> logger.info { "${payloadRequest.deviceName} is online at $timestamp" }
                    "offline" -> logger.info { "${payloadRequest.deviceName} is offline at $timestamp" }
                }
            }

            "debug" -> {
                logger.info { "${payloadRequest.deviceName} at ${timestamp.format(DateTimeFormatter.ISO_TIME)}: $data" }
            }

            // Suppress the logs for known sensor values but ones that we aren't recording at the moment
            "ip_address", "mac_address", "connected_ssid", "button" -> {}

            else -> {
                logger.warn { "Unknown or unimplemented sensor: ${payloadRequest.sensor}" }
            }
        }
    }

    companion object {
        /**
         * Initializer for the HTTP header.
         *
         * @return an instance of [HttpHeaders] with `contentType` set to `application/json`
         */
        private fun initHttpHeaders(): HttpHeaders {
            val httpHeaders = HttpHeaders()
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            return httpHeaders
        }
    }
}