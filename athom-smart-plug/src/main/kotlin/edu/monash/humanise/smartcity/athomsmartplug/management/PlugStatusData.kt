package edu.monash.humanise.smartcity.athomsmartplug.management




data class PlugStatusData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val isOn: Boolean
)