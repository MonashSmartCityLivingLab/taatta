package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

@Entity
class UptimePayload(
        deviceName: String,
        data: String,
        val uptime: Long?
) : Payload(0, deviceName, data)