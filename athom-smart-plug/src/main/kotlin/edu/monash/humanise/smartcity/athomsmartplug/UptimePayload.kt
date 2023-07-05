package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity

/**
 * Sensor datapoint entity for uptime readings.
 */
@Entity
class UptimePayload(
    deviceName: String,
    data: String,
    /**
     * Uptime, in seconds.
     */
    val uptime: Long?
) : Payload(0, deviceName, data)