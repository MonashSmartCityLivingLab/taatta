package edu.monash.humanise.smartcity;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Base64;

@Slf4j
@Getter
public class Decoder {
    private final String encoded;

    private double temperature;
    private double tds;
    public Decoder(String encoded) {
        this.encoded = encoded;
    }

    public void decode() {
        int tmp;
        byte[] decoded;
        DecimalFormat decFormatTwoDigit = new DecimalFormat("0.00");

        decoded = Base64.getDecoder().decode(this.encoded);
        log.info("In the decoder class with encoded value {}", this.encoded);
        log.info("decoded value {}", decoded);
        // Channel 1 is for temperature sensor.
        if((decoded[0] & 0xFF) == 0x1) {
            // temperature code is 0x67 provided by Cayenne LPP (Low Power Payload)
            if((decoded[1] & 0xFF) == 0x67) {
                tmp = decoded[3] & 0xFF;
                tmp = (tmp << 8);
                tmp = tmp | (decoded[2] & 0xFF);

                log.info("The temperature bytes is {} and {} value is {}", decoded[2], decoded[3], (tmp / 16));

                // using two's compliment than divided by 16
                this.temperature = Double.valueOf(decFormatTwoDigit.format(tmp / 16));
            } else {
                log.warn("First sensor code {} is not valid.", decoded[1]);
            }
        } else {
            log.error("First channel number {} is not valid", decoded[0]);
        }

        // Channel 2 is for temperature sensor.
        if((decoded[4] & 0xFF) == 0x2) {
            // temperature code is 0x67 provided by Cayenne LPP (Low Power Payload)
            if((decoded[5] & 0xFF) == 0x68) {
                // using two's compliment than divided by 16
                tmp = (decoded[6] & 0xFF);
                tmp = (tmp << 8);
                tmp = tmp | (decoded[7] & 0xFF);
                log.info("The TDS bytes is {} and {} value is {}", decoded[6], decoded[7], tmp);
                this.tds = Double.valueOf(decFormatTwoDigit.format(tmp));
            } else {
                log.warn("Second sensor code {} is not valid.", decoded[1]);
            }
        } else {
            log.error("Second channel number {} is not valid", decoded[0]);
        }
    }
}
