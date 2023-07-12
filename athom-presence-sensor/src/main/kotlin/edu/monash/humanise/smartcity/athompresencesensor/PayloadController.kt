package edu.monash.humanise.smartcity.athompresencesensor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

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
        try {
            payloadService.decodeUplinkPayload(payloadRequest)
        } catch (e: DecoderException) {
            val msg = e.message
            logger.error(e) { msg }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, msg, e)
        } catch (e: NotImplementedError) {
            val msg = e.message
            logger.error(e) { msg }
            throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, msg, e)
        }
    }
}