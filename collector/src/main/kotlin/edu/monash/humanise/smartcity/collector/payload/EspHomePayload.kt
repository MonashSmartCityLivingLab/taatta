package edu.monash.humanise.smartcity.collector.payload

import kotlinx.serialization.Serializable

/**
 * Payload body for ESPHome devices.
 */
@Serializable
data class EspHomePayload(
    /**
     * Timestamp of datapoint according to MQTT timestamp, or when the message arrived at the collector if the timestamp is not available.
     */
    val timestampMilliseconds: Long,
    /**
     * Device name. This corresponds to the `esphome.name` attribute in the device's yml file.
     */
    val deviceName: String,
    /**
     * Sensor name.
     */
    val sensor: String,
    /**
     * Payload data as string.
     */
    val data: String
)
