package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for daily energy consumption readings.
 */
@Entity
class DailyEnergyConsumptionPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Daily energy consumption, in kWh.
     */
    val dailyEnergyConsumption: Double?
) : Payload(deviceName, timestamp, data)