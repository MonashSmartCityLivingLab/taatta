package edu.monash.humanise.smartcity;

public record PayloadUplinkRequest(String deviceName, String devEUI, String data) {
}
