package edu.monash.humanise.smartcity.pcr2;

public record PayloadUplinkRequest(String deviceName, String devEUI, String data) {
}
