package edu.monash.humanise.smartcity;


import java.text.DecimalFormat;
import java.util.Base64;

public class Decoder {
    private final String encoded;

    private int frameCount;
    private int status;
    private boolean parkFlag;
    private int battery;

    public Decoder(String encoded) {
        this.encoded = encoded;
    }

    public void decode() throws Exception {
        byte[] decoded;

        decoded = Base64.getDecoder().decode(this.encoded);

        int type = (decoded[0] & 0xFF);
        if(type != 0xab) {
            throw new Exception("Frame type " + type + " not required to save yet.");
        }

        int tmp = (decoded[1] & 0xFF);
        this.frameCount = tmp & 0xF0;
        this.status = tmp & 0x0F;

        tmp = (decoded[2] & 0xFF);
        this.parkFlag = (((tmp & 0x80) > 0) ? true : false);
        this.battery = (tmp & 0x7F);
    }

    public boolean isParkFlag() {
        return parkFlag;
    }

    public int getFrameCount() {
        return frameCount;
    }

    public int getStatus() {
        return status;
    }

    public int getBattery() {
        return battery;
    }
}
