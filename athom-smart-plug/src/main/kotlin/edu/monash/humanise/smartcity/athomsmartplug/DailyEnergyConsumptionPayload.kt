package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

/**
 * Sensor datapoint entity for daily energy consumption readings.
 */
@Entity
class DailyEnergyConsumptionPayload(
        deviceName: String,
        data: String,
        /**
         * Daily energy consumption, in kWh.
         */
        val dailyEnergyConsumption: Double?
) : Payload(0, deviceName, data)