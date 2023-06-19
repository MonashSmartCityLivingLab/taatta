package edu.monash.humanise.smartcity.pcr2

import io.github.oshai.KotlinLogging
import lombok.AllArgsConstructor
import lombok.extern.slf4j.Slf4j
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
        logger.debug { "New uplink payload to decode: $payloadRequest" }
        payloadService.decodeUplinkPayload(payloadRequest)
    }
}
