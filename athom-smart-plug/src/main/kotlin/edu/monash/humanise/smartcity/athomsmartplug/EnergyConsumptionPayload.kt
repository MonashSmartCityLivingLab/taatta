package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class EnergyConsumptionPayload(
        deviceName: String,
        data: String,
        val energyConsumption: Double?
) : Payload(0, deviceName, data)