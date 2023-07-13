package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for power readings.
 */
@Entity
class PowerPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Power, in watts.
     */
    @Column(nullable = false)
    val power: Double
) : Payload(deviceName, timestamp, data)