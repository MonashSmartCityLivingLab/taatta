package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for voltage readings.
 */
@Entity
class VoltagePayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Voltage, in volts.
     */
    @Column(nullable = false)
    val voltage: Double
) : Payload(deviceName, timestamp, data)