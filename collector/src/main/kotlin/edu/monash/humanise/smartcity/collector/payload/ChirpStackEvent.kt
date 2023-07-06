package edu.monash.humanise.smartcity.collector.payload

import kotlinx.serialization.Serializable

@Serializable
data class ChirpStackEvent(
    val deviceName: String,
    val deviceProfileName: String,
    val deviceProfileID: String,
    val devEUI: String,
    val data: String? = null
)
