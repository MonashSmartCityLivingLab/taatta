package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

/**
 * Sensor datapoint entity for total energy consumption readings.
 */
@Entity
class TotalEnergyConsumptionPayload(
        deviceName: String,
        data: String,
        /**
         * Total energy consumption, in kWh.
         */
        val totalEnergyConsumption: Double?
) : Payload(0, deviceName, data)