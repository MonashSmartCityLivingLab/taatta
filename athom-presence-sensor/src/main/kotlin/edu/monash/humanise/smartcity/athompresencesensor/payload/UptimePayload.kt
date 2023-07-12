package edu.monash.humanise.smartcity.athompresencesensor.payload

import edu.monash.humanise.smartcity.athompresencesensor.payload.Payload
import jakarta.persistence.Column
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
    @Column(nullable = false)
    val uptime: Long
) : Payload(deviceName, timestamp, data)