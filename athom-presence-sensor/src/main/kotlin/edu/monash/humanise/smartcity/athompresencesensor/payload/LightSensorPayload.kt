package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for light sensor readings.
 */
@Entity
class LightSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Luminance, in lux.
     */
    @Column(nullable = false)
    val luminance: Double
) : Payload(deviceName, timestamp, data)