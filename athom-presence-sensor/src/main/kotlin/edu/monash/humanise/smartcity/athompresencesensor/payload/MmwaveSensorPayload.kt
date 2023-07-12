package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for PIR sensor readings.
 */
@Entity
class MmwaveSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Occupancy detection according to this sensor.
     */
    @Column(nullable = false)
    val occupied: Boolean,
): Payload(deviceName, timestamp, data)