package edu.monash.humanise.smartcity.athomsmartplug

import io.github.oshai.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

private val logger = KotlinLogging.logger {}


/**
 * Spring controller for payload POST endpoint to receive data from the collector.
 */
@RestController
@RequestMapping("/api/payload")
class PayloadController(
    private val payloadService: PayloadService,
    private val stats: Stats
) {

    /**
     * POST endpoint to receive data from the collector.
     */
    @PostMapping
    fun decodeUplinkPayload(@RequestBody payloadRequest: PayloadUplinkRequest) {
        logger.debug { "New uplink payload to decode $payloadRequest" }
        stats.incrementCounter(payloadRequest.deviceName)
        payloadService.decodeUplinkPayload(payloadRequest)
    }
}