package edu.monash.humanise.smartcity.pcr2

import org.springframework.stereotype.Service

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        val decoded = Decoder.decode(payloadRequest.data)
        val payload = Payload(
            payloadRequest.deviceName,
            payloadRequest.data,
            payloadRequest.devEUI,
            decoded.ltr,
            decoded.rtl,
            decoded.cpuTemp
        )
        payloadRepository.save(payload)
    }
}
