package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity

/**
 * Sensor datapoint entity for current readings.
 */
@Entity
class CurrentPayload(
        deviceName: String,
        data: String,
        /**
         * Current, in amps.
         */
        val current: Double?
) : Payload(0, deviceName, data)