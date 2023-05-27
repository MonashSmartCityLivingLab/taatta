package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.stereotype.Service

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
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
            else -> {

            }
        }
    }
}