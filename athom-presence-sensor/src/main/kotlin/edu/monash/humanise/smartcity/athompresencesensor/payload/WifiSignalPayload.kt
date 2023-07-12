package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for Wi-Fi signal readings.
 */
@Entity
class WifiSignalPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Wi-Fi signal strength, in dBm.
     */
    @Column(nullable = false)
    val signalStrength: Int
) : Payload(deviceName, timestamp, data)