package edu.monash.humanise.smartcity.collector.payload



/**
 * Payload body for LoRaWAN devices.
 */

data class LoRaPayload(
    /**
     * Timestamp of datapoint according to MQTT timestamp, or when the message arrived at the collector if the timestamp is not available.
     */
    val timestampMilliseconds: Long,
    val deviceName: String,
    val devEUI: String,
    val data: String
)