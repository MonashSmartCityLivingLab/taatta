package edu.monash.humanise.smartcity.pcr2

import org.springframework.stereotype.Service
import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        val timestamp =
            OffsetDateTime.ofInstant(Instant.ofEpochMilli(payloadRequest.timestampMilliseconds), ZoneOffset.UTC)
        val decoded = Decoder.decode(payloadRequest.data)
        val payload = Payload(
            payloadRequest.deviceName,
            payloadRequest.data,
            payloadRequest.devEUI,
            timestamp,
            decoded.ltr,
            decoded.rtl,
            decoded.cpuTemp
        )
        payloadRepository.save(payload)
    }
}
