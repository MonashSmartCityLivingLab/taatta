package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class VoltagePayload(
        deviceName: String,
        data: String,
        val voltage: Double?
) : Payload(0, deviceName, data)