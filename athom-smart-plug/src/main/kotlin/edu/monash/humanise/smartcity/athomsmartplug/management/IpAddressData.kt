package edu.monash.humanise.smartcity.athomsmartplug.management

data class IpAddressData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val ipAddress: String
)