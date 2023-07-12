package edu.monash.humanise.smartcity.athomsmartplug

import kotlinx.serialization.Serializable

/**
 * Payload data sent from the collector.
 */
@Serializable
data class PayloadUplinkRequest(
    /**
     * Timestamp of data point, in milliseconds since 1970-01-01T00:00:00Z.
     */
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
     * Payload data as string.
     */
    val data: String
)
