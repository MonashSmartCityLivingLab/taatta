package edu.monash.humanise.smartcity.pcr2

import io.github.oshai.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        try {
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
        } catch (e: Exception) {
            logger.error(e) { "Got exception in service." }
        }
    }
}
