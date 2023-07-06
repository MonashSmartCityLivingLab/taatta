package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Service
@AllArgsConstructor
public class PayloadService {
    private final PayloadRepository payloadRepository;

    public void decodeUplinkPayload(PayloadUplinkRequest payloadRequest) {
        var timestamp = OffsetDateTime.ofInstant(Instant.ofEpochMilli(payloadRequest.timestampMilliseconds()), ZoneOffset.UTC);
        Decoder decoder = new Decoder(payloadRequest.data());
        decoder.decode();

        Payload payload = Payload.builder()
                .deviceName(payloadRequest.deviceName())
                .data(payloadRequest.data())
                .devEUI(payloadRequest.devEUI())
                .temperature(decoder.getTemperature())
                .tds(decoder.getTds())
                .timestamp(timestamp)
                .build();
        payloadRepository.save(payload);
    }
}
