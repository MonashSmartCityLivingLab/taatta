package edu.monash.humanise.smartcity.athomsmartplug.management

import kotlinx.serialization.Serializable


@Serializable
data class PowerData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val power: Double
)