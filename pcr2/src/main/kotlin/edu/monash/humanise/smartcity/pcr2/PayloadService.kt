package edu.monash.humanise.smartcity.pcr2;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class PayloadService {
    private final PayloadRepository payloadRepository;
    public void decodeUplinkPayload(PayloadUplinkRequest payloadRequest) {
        try{
            Decoder decoder = new Decoder(payloadRequest.data());
            decoder.decode();

            Payload payload = Payload.builder()
                    .deviceName(payloadRequest.deviceName())
                    .data(payloadRequest.data())
                    .devEUI(payloadRequest.devEUI())
                    .ltr(decoder.getLtr())
                    .rtl(decoder.getRtl())
                    .cpuTemp(decoder.getCpuTemp())
                    .build();
            payloadRepository.save(payload);

        } catch (Exception e) {
            log.error("Got exception in service. " + e.getMessage());
        }
    }
}
