package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for energy consumption readings.
 */
@Entity
class EnergyConsumptionPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Energy consumption, in kWh.
     */
    @Column(nullable = false)
    val energyConsumption: Double
) : Payload(deviceName, timestamp, data)