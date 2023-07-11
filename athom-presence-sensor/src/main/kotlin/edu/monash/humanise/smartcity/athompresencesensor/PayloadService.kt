package edu.monash.humanise.smartcity.athompresencesensor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        val timestamp =
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(payloadRequest.timestampMilliseconds), ZoneOffset.UTC)
        when (payloadRequest.sensor) {
            "pir_sensor" -> {
                val value = payloadRequest.data.toBooleanStrictOrNull()
                val payload = PirSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "mm_wave_sensor" -> {
                val value = payloadRequest.data.toBooleanStrictOrNull()
                val payload = MmWaveSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "occupancy" -> {
                val value = payloadRequest.data.toBooleanStrictOrNull()
                val payload = OccupancyPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "uptime_sensor" -> {
                val value = payloadRequest.data.toLongOrNull()
                val payload = UptimePayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "wi_fi_signal_sensor" -> {
                val value = payloadRequest.data.toIntOrNull()
                val payload = WiFiSignalPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
                payloadRepository.save(payload)
            }

            "light_sensor" -> {
                val value = payloadRequest.data.toDoubleOrNull()
                val payload = LightSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
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