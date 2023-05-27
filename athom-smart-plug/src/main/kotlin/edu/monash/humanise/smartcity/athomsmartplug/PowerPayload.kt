package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class PowerPayload(
        deviceName: String,
        data: String,
        val power: Double?
) : Payload(0, deviceName, data)