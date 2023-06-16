package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity

/**
 * Sensor datapoint entity for energy consumption readings.
 */
@Entity
class EnergyConsumptionPayload(
        deviceName: String,
        data: String,
        /**
         * Energy consumption, in kWh.
         */
        val energyConsumption: Double?
) : Payload(0, deviceName, data)