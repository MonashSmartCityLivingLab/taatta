package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for uptime readings.
 */
@Entity
class UptimePayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Uptime, in seconds.
     */
    val uptime: Long?
) : Payload(deviceName, timestamp, data)