package edu.monash.humanise.smartcity.pcr2




data class PayloadUplinkRequest(
    val timestampMilliseconds: Long,
    val deviceName: String,
    val devEUI: String,
    val data: String
)
