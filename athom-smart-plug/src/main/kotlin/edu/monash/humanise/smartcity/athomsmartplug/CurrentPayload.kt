package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class CurrentPayload(
        deviceName: String,
        data: String,
        val current: Double?
): Payload(0, deviceName, data)