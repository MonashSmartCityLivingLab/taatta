package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for uptime readings.
 */
@Entity
class LightSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Uptime, in seconds.
     */
    @Column(nullable = false)
    val luminance: Double
) : Payload(deviceName, timestamp, data)