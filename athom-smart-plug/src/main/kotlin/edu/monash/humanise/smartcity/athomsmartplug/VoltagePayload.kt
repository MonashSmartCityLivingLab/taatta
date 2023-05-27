package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class VoltagePayload(
        deviceName: String,
        val voltage: Double?
): Payload(0, deviceName)