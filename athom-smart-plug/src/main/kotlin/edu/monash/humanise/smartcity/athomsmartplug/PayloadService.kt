package edu.monash.humanise.smartcity.athomsmartplug

import edu.monash.humanise.smartcity.athomsmartplug.payload.*
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

/**
 * Service for storing smart plug sensor data.
 */
@Service
class PayloadService(private val payloadRepository: PayloadRepository) {

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

            // Suppress the logs for known sensor values but ones that we aren't recording at the moment
            "ip_address", "mac_address", "connected_ssid" -> {}

            else -> {
                logger.warn { "Unknown or unimplemented sensor: ${payloadRequest.sensor}" }
            }
        }
    }
}