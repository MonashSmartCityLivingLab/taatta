package edu.monash.humanise.smartcity.athomsmartplug

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
    val energyConsumption: Double?
) : Payload(deviceName, timestamp, data)