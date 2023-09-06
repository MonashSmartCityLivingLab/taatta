package edu.monash.humanise.smartcity.athomsmartplug

import edu.monash.humanise.smartcity.athomsmartplug.management.IpAddressData
import edu.monash.humanise.smartcity.athomsmartplug.management.PlugStatusData
import edu.monash.humanise.smartcity.athomsmartplug.management.PowerData
import edu.monash.humanise.smartcity.athomsmartplug.payload.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

private val logger = KotlinLogging.logger {}

/**
 * Service for storing smart plug sensor data.
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
            "athom_smart_plug_v2_voltage" -> {
                val voltage = Decoder.decodeFloatSensorState(data)
                val payload = VoltagePayload(payloadRequest.deviceName, timestamp, payloadRequest.data, voltage)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_current" -> {
                val current = Decoder.decodeFloatSensorState(data)
                val payload = CurrentPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, current)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_power" -> {
                val power = Decoder.decodeFloatSensorState(data)
                val payload = PowerPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, power)
                payloadRepository.save(payload)

                // send data to management
                if (deviceManagementEnabled) {
                    val restTemplate = RestTemplate()
                    val url = "$deviceManagementUrl/power"
                    val headers = initHttpHeaders()
                    val body = PowerData(payloadRequest.timestampMilliseconds, payloadRequest.deviceName, power)
                    val request = HttpEntity(body, headers)
                    try {
                        restTemplate.postForEntity(url, request, String::class.java)
                    } catch (e: RestClientException) {
                        logger.error(e) { "Cannot send occupancy data" }
                    }
                }
            }

            "athom_smart_plug_v2_energy" -> {
                val energy = Decoder.decodeFloatSensorState(data)
                val payload =
                    EnergyConsumptionPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_total_energy" -> {
                val energy = Decoder.decodeFloatSensorState(data)
                val payload =
                    TotalEnergyConsumptionPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_total_daily_energy" -> {
                val energy = Decoder.decodeFloatSensorState(data)
                val payload =
                    DailyEnergyConsumptionPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_uptime_sensor" -> {
                val uptime = Decoder.decodeLongSensorState(data)
                val payload = UptimePayload(payloadRequest.deviceName, timestamp, payloadRequest.data, uptime)
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

            "athom_smart_plug_v2_power_button" -> {
                val isOn = Decoder.decodeBinarySensorState(payloadRequest.data)
                if (deviceManagementEnabled) {
                    val restTemplate = RestTemplate()
                    val url = "$deviceManagementUrl/plug-status"
                    val headers = initHttpHeaders()
                    val body = PlugStatusData(payloadRequest.timestampMilliseconds, payloadRequest.deviceName, isOn)
                    val request = HttpEntity(body, headers)
                    try {
                        restTemplate.postForEntity(url, request, String::class.java)
                    } catch (e: RestClientException) {
                        logger.error(e) { "Cannot send occupancy data" }
                    }
                }
            }

            "ip_address" -> {
                if (deviceManagementEnabled) {
                    val restTemplate = RestTemplate()
                    val url = "$deviceManagementUrl/ip-address"
                    val headers = initHttpHeaders()
                    val body = IpAddressData(payloadRequest.timestampMilliseconds, payloadRequest.deviceName, payloadRequest.data)
                    val request = HttpEntity(body, headers)
                    try {
                        restTemplate.postForEntity(url, request, String::class.java)
                    } catch (e: RestClientException) {
                        logger.error(e) { "Cannot send occupancy data" }
                    }
                }
            }

            // Suppress the logs for known sensor values but ones that we aren't recording at the moment
            "mac_address", "connected_ssid" -> {}

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