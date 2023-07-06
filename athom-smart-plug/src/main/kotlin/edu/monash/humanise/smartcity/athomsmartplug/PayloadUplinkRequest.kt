package edu.monash.humanise.smartcity.athomsmartplug

import kotlinx.serialization.Serializable

/**
 * Payload data send from the collector.
 */
@Serializable
data class PayloadUplinkRequest(
    val timestampMilliseconds: Long,
    /**
     * Device name. This corresponds to the `esphome.name` attribute in the plug's yml file.
     */
    val deviceName: String,
    /**
     * Sensor name. See the module README for a list of known sensor name.
     */
    val sensor: String,
    /**
     * Payload data.
     */
    val data: String
)
