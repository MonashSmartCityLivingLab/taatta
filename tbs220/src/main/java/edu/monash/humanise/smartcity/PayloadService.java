package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Slf4j
@Service
@AllArgsConstructor
public class PayloadService {
    private final PayloadRepository payloadRepository;

    public void decodeUplinkPayload(PayloadUplinkRequest payloadRequest) {
        try {
            var timestamp = OffsetDateTime.ofInstant(Instant.ofEpochMilli(payloadRequest.timestampMilliseconds()), ZoneOffset.UTC);
            Decoder decoder = new Decoder(payloadRequest.data());
            decoder.decode();

            Payload payload = Payload.builder()
                    .deviceName(payloadRequest.deviceName())
                    .data(payloadRequest.data())
                    .devEUI(payloadRequest.devEUI())
                    .frameCount(decoder.getFrameCount())
                    .status(decoder.getStatus())
                    .parkFlag(decoder.isParkFlag())
                    .battery(decoder.getBattery())
                    .timestamp(timestamp)
                    .build();
            payloadRepository.save(payload);
        } catch (Exception e) {
            log.error("Got exception in service. " + e.getMessage());
        }
    }
}
