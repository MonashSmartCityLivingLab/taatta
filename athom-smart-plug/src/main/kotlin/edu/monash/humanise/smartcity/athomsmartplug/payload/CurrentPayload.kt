package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for current readings.
 */
@Entity
class CurrentPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Current, in amps.
     */
    @Column(nullable = false)
    val current: Double
) : Payload(deviceName, timestamp, data)