package edu.monash.humanise.smartcity.athomsmartplug.management

import kotlinx.serialization.Serializable

@Serializable
data class PlugStatusData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val isOn: Boolean
)