package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/payload")
class PayloadController {

    @PostMapping
    fun decodeUplinkPayload(@RequestBody payloadRequest: PayloadUplinkRequest) {
        println("New payload: $payloadRequest")
    }
}