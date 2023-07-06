package edu.monash.humanise.smartcity.athompresencesensor

import org.springframework.stereotype.Service

@Service
class PayloadService(private val payloadRepository: PayloadRepository) {
    fun decodeUplinkPayload(payloadRequest: PayloadUplinkRequest) {
        TODO()
    }
}