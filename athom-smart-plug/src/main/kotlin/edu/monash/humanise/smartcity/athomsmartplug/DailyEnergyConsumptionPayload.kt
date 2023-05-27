package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class DailyEnergyConsumptionPayload(
        deviceName: String,
        data: String,
        val dailyEnergyConsumption: Double?
) : Payload(0, deviceName, data)