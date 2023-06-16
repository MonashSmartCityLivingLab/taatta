package edu.monash.humanise.smartcity.collector.payload

import kotlinx.serialization.Serializable

@Serializable
data class EspHomePayload(
        val deviceName: String,
        val sensor: String,
        val data: String
)
