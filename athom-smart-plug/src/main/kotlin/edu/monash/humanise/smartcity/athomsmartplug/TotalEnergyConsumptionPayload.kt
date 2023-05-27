package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class TotalEnergyConsumptionPayload(
        deviceName: String,
        data: String,
        val totalEnergyConsumption: Double?
) : Payload(0, deviceName, data)