package edu.monash.humanise.smartcity.athomsmartplug

data class PayloadUplinkRequest(
        val deviceName: String = "",
        val sensor: String = "",
        val data: String = ""
)
