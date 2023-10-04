package edu.monash.humanise.smartcity.collector.chirpstack.events

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


/**
 * ChirpStack uplink event.
 *
 * See [ChirpStack documentation](https://www.chirpstack.io/application-server/integrations/events/#json) for example
 * data.
 */

@JsonIgnoreProperties(ignoreUnknown = true)
data class ChirpStackUplinkEvent(
    val deviceName: String,
    val deviceProfileName: String,
    val deviceProfileID: String,
    val devEUI: String,
    val data: String? = null
)
