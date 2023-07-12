package edu.monash.humanise.smartcity.athomsmartplug.payload

import jakarta.persistence.Column
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
    @Column(nullable = false)
    val dailyEnergyConsumption: Double
) : Payload(deviceName, timestamp, data)