package edu.monash.humanise.smartcity.athomsmartplug.management





data class PowerData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val power: Double
)