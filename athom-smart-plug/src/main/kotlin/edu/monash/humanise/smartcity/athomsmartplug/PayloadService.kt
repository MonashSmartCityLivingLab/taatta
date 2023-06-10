package edu.monash.humanise.smartcity.athomsmartplug

import io.github.oshai.KotlinLogging
import org.springframework.stereotype.Service

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
        when (payloadRequest.sensor) {
            "athom_smart_plug_v2_voltage" -> {
                val voltage = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = VoltagePayload(payloadRequest.deviceName, payloadRequest.data, voltage)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_current" -> {
                val current = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = CurrentPayload(payloadRequest.deviceName, payloadRequest.data, current)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_power" -> {
                val power = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = PowerPayload(payloadRequest.deviceName, payloadRequest.data, power)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_energy" -> {
                val energy = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = EnergyConsumptionPayload(payloadRequest.deviceName, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_total_energy" -> {
                val energy = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = TotalEnergyConsumptionPayload(payloadRequest.deviceName, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_total_daily_energy" -> {
                val energy = try {
                    payloadRequest.data.toDouble()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = DailyEnergyConsumptionPayload(payloadRequest.deviceName, payloadRequest.data, energy)
                payloadRepository.save(payload)
            }

            "athom_smart_plug_v2_uptime_sensor" -> {
                val uptime = try {
                    payloadRequest.data.toLong()
                } catch (e: NumberFormatException) {
                    null
                }
                val payload = UptimePayload(payloadRequest.deviceName, payloadRequest.data, uptime)
                payloadRepository.save(payload)
            }

            // Suppress the logs for known sensor values but ones that we aren't recording at the moment
            "ip_address", "mac_address", "connected_ssid" -> {}

            else -> {
                logger.warn { "Unknown or unimplemented sensor: ${payloadRequest.sensor}" }
            }
        }
    }
}