package edu.monash.humanise.smartcity.athomsmartplug

import io.github.oshai.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/payload")
class PayloadController(private val payloadService: PayloadService) {
    @PostMapping
    fun decodeUplinkPayload(@RequestBody payloadRequest: PayloadUplinkRequest) {
        logger.info { "New uplink payload to decode $payloadRequest" }
        payloadService.decodeUplinkPayload(payloadRequest)
    }
}