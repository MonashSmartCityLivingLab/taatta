package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for total energy consumption readings.
 */
@Entity
class TotalEnergyConsumptionPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Total energy consumption, in kWh.
     */
    @Column(nullable = false)
    val totalEnergyConsumption: Double
) : Payload(deviceName, timestamp, data)