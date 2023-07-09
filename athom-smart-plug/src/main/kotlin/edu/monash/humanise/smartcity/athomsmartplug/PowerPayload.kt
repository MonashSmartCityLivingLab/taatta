package edu.monash.humanise.smartcity.athomsmartplug

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
    val power: Double?
) : Payload(deviceName, timestamp, data)