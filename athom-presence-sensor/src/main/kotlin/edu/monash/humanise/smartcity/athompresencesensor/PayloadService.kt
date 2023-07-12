package edu.monash.humanise.smartcity.athompresencesensor

import edu.monash.humanise.smartcity.athompresencesensor.payload.*
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
        val data = payloadRequest.data
        when (payloadRequest.sensor) {
            "pir_sensor" -> {
                val value = Decoder.decodeBinarySensorState(data)
                val payload = PirSensorPayload(payloadRequest.deviceName, timestamp, payloadRequest.data, value)
//                payloadRepository.save(payload)
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

            // Suppress the logs for known sensor values but ones that we aren't recording at the moment
            "ip_address", "mac_address", "connected_ssid", "button" -> {}

            else -> {
                logger.warn { "Unknown or unimplemented sensor: ${payloadRequest.sensor}" }
            }
        }
    }
}