package edu.monash.humanise.smartcity;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PayloadService {
    private final PayloadRepository payloadRepository;
    public void decodeUplinkPayload(PayloadUplinkRequest payloadRequest) {
        Decoder decoder = new Decoder(payloadRequest.data());
        decoder.decode();

        Payload payload = Payload.builder()
                .deviceName(payloadRequest.deviceName())
                .data(payloadRequest.data())
                .devEUI(payloadRequest.devEUI())
                .battery(decoder.isBattery())
                .isFull(decoder.isFull())
                .fire(decoder.isFire())
                .level(decoder.getLevel())
                .temperature(decoder.getTemperature())
                .build();
        payloadRepository.save(payload);
    }
}
