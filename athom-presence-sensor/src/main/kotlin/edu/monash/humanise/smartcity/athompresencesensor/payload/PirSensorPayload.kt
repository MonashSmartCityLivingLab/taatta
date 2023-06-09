package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for PIR sensor readings.
 *
 * @see OccupancyPayload
 * @see MmwaveSensorPayload
 */
@Entity
class PirSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Occupancy detection according to this sensor.
     */
    @Column(nullable = false)
    val occupied: Boolean
): Payload(deviceName, timestamp, data)