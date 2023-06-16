package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.Entity

/**
 * Sensor datapoint entity for power readings.
 */
@Entity
class PowerPayload(
        deviceName: String,
        data: String,
        /**
         * Power, in watts.
         */
        val power: Double?
) : Payload(0, deviceName, data)