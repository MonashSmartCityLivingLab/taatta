package edu.monash.humanise.smartcity;

public record PayloadUplinkRequest(long timestampMilliseconds, String deviceName, String devEUI, String data) {
}
