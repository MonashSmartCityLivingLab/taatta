package edu.monash.humanise.smartcity.athompresencesensor

import kotlinx.serialization.Serializable

/**
 * Payload data send from the collector.
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
     * Payload data.
     */
    val data: String
)
