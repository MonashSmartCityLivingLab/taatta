package edu.monash.humanise.smartcity.pcr2

import io.github.oshai.KotlinLogging
import org.springframework.stereotype.Service

private val logger = KotlinLogging.logger {}

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        try {
            val decoderOld = DecoderOld(payloadRequest.data)
            decoderOld.decode()
            val payload = Payload(
                payloadRequest.deviceName,
                payloadRequest.data,
                payloadRequest.devEUI,
                decoderOld.ltr,
                decoderOld.rtl,
                decoderOld.cpuTemp
            )
            payloadRepository.save(payload)
        } catch (e: Exception) {
            logger.error(e) { "Got exception in service." }
        }
    }
}
