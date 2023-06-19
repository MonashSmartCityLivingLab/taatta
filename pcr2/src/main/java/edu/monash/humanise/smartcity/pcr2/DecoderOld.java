package edu.monash.humanise.smartcity.pcr2;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.text.DecimalFormat;
import java.util.Base64;

@Slf4j
@Getter
public class DecoderOld {
    private final String encoded;
    int ltr;
    int rtl;
    int cpuTemp;

    public DecoderOld(String encoded) {
        this.encoded = encoded;
    }

    public void decode() throws Exception {
        byte[] decoded;
        DecimalFormat decFormatTwoDigit = new DecimalFormat("#.##");

        decoded = Base64.getDecoder().decode(this.encoded);
        int type = (decoded[0] & 0xFF);

        if(type == 0x0a) {
            // decode ELSYS payload
            log.info("Payload type is ELSYS");

            ltr = (decoded[1] & 0xFF);
            ltr = (ltr << 8) | (decoded[2] & 0xFF);
            log.info("Left to right people count is {}", ltr);

            rtl = (decoded[4] & 0xFF);
            rtl = (rtl << 8) | (decoded[5] & 0xFF);
            log.info("Right to left people count is {}", rtl);

            cpuTemp = (decoded[7] & 0xFF);
            cpuTemp = (cpuTemp << 8) | (decoded[8] & 0xFF);
            log.info("Temperature is {}c", cpuTemp);

        } else if (type == 0 && (decoded[1] & 0xFF) == 102) {
            // decode LPP payload
            log.info("Payload type is LPP");
            throw new Exception("Payload type LPP not handled yet.");
        } else if (type == 0xbe && ((decoded[1] & 0xFF) == 0x01) && ((decoded[2] & 0xFF) == 0x03)) {
            // decode extended payload V3
            log.info("Payload type is extended payload V3");
            throw new Exception("Extended payload V3 not handled yet.");
        } else {
            // payload type not recognize.
            log.error("Payload type can't recognize");
            throw new Exception("Payload type can't recognize");
        }
    }
}
