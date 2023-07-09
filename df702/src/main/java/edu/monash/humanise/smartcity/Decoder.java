package edu.monash.humanise.smartcity;


import java.text.DecimalFormat;
import java.util.Base64;

public class Decoder {
    private final String encoded;

    private int level;
    private boolean full;
    private boolean fire;
    //    private boolean tilt;
    private boolean battery;
    //    private double angle;
    private int temperature;

    public Decoder(String encoded) {
        this.encoded = encoded;
    }

    public void decode() {
        byte[] decoded;
        DecimalFormat decFormatTwoDigit = new DecimalFormat("#.##");

        decoded = Base64.getDecoder().decode(this.encoded);

        int fstData = (decoded[5] & 0xff);
        int sndData = (decoded[6] & 0xff);

        this.level = (fstData << 8) | sndData;

        fstData = (decoded[11] & 0xff);
        if ((fstData >> 4) > 0) {
            this.full = true;
        } else {
            this.full = false;
        }

        if ((fstData & 0x0f) > 0) {
            this.fire = true;
        } else {
            this.fire = false;
        }

        fstData = (decoded[12] & 0xff);
        if ((fstData & 0x0f) > 0) {
            this.battery = true;
        } else {
            this.battery = false;
        }

        int data = (decoded[16] & 0xff);
        this.temperature = data;

    }


    public boolean isBattery() {
        return battery;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isFire() {
        return fire;
    }

    public int getTemperature() {
        return temperature;
    }

    public int getLevel() {
        return level;
    }
}
