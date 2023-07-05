package edu.monash.humanise.smartcity.pcr2

import io.github.oshai.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

private val logger = KotlinLogging.logger {}

@RestController
@RequestMapping("/api/payload")
class PayloadController(private val payloadService: PayloadService) {

    @PostMapping
    fun decodeUplinkPayload(@RequestBody payloadRequest: PayloadUplinkRequest) {
        logger.debug { "New uplink payload to decode: $payloadRequest" }
        try {
            payloadService.decodeUplinkPayload(payloadRequest)
        } catch (e: DecoderException) {
            val msg = e.message
            logger.error(e) { msg }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, msg, e)
        } catch (e: NotImplementedError) {
            val msg = "Cannot decode payload with data ${payloadRequest.data}: Payload type LPP not supported yet"
            logger.error(e) { msg }
            throw ResponseStatusException(HttpStatus.NOT_IMPLEMENTED, msg, e)
        }
    }
}
