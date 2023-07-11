package edu.monash.humanise.smartcity.collector.payload

import kotlinx.serialization.Serializable

@Serializable
data class LoRaPayload(
    val timestampMilliseconds: Long,
    val deviceName: String,
    val devEUI: String,
    val data: String
)