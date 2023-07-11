package edu.monash.humanise.smartcity.collector.payload

import kotlinx.serialization.Serializable

@Serializable
data class EspHomePayload(
    val timestampMilliseconds: Long,
    val deviceName: String,
    val sensor: String,
    val data: String
)
