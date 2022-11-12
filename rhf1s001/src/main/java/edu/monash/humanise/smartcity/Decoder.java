package edu.monash.humanise.smartcity;


import java.text.DecimalFormat;
import java.util.Base64;

public class Decoder {
    private final String encoded;

    private double humidity;
    private double temperature;
    private int period;
    private double batteryLevel;

    public Decoder(String encoded) {
        this.encoded = encoded;
    }

    public void decode() {
        byte[] decoded;
        DecimalFormat decFormatTwoDigit = new DecimalFormat("#.##");

        decoded = Base64.getDecoder().decode(this.encoded);

        int tmp = (decoded[3] & 0xFF);
        this.humidity = ((tmp * 125) / 256) - 6;
        this.humidity = Double.valueOf(decFormatTwoDigit.format(this.humidity));

        tmp = decoded[2] & 0xFF;
        tmp = ((tmp << 8) | (decoded[1] & 0xFF));
        this.temperature = ((tmp * 175.72) / 65536) - 46.85;
        this.temperature = Double.valueOf(decFormatTwoDigit.format(this.temperature));

        tmp = decoded[5] & 0xFF;
        tmp = (tmp << 8) | (decoded[4] & 0xFF);
        this.period = tmp * 2;

        tmp = decoded[8] & 0xFF;
        this.batteryLevel = (tmp + 150) * 0.01;
        this.batteryLevel = Double.valueOf(decFormatTwoDigit.format(this.batteryLevel));
    }

    public double getHumidity() {
        return this.humidity;
    }

    public double getTemperature() {
        return this.temperature;
    }

    public int getPeriod() {
        return this.period;
    }

    public double getBatteryLevel() {
        return this.batteryLevel;
    }
}
