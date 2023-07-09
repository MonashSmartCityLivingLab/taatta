package edu.monash.humanise.smartcity.collector.chirpstack.events

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.SerializationException

/**
 * ChirpStack uplink event.
 *
 * See [ChirpStack documentation](https://www.chirpstack.io/application-server/integrations/events/#json) for example
 * data.
 *
 * Note that not all properties are included, so you must set `ignoreUnknownKeys` to true for your [Json] decoder, or it
 * will throw [SerializationException] when decoding events.
 */
@Serializable
data class ChirpStackUplinkEvent(
    val deviceName: String,
    val deviceProfileName: String,
    val deviceProfileID: String,
    val devEUI: String,
    val data: String? = null
)
