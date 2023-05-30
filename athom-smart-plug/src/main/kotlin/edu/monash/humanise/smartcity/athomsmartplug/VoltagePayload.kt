package edu.monash.humanise.smartcity.athomsmartplug

import javax.persistence.Entity

/**
 * Sensor datapoint entity for voltage readings.
 */
@Entity
class VoltagePayload(
        deviceName: String,
        data: String,
        /**
         * Voltage, in volts.
         */
        val voltage: Double?
) : Payload(0, deviceName, data)