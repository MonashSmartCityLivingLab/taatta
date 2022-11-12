package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/payload")
@AllArgsConstructor
public class PayloadController {
    private final PayloadService payloadService;

    @PostMapping
    public void decodeUplinkPayload(@RequestBody PayloadUplinkRequest payloadRequest) {
        log.info("New uplink payload to decode {}", payloadRequest);
        payloadService.decodeUplinkPayload(payloadRequest);
    }
}
