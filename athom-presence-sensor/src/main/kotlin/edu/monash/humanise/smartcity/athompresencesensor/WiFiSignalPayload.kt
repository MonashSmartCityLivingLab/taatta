package edu.monash.humanise.smartcity.athompresencesensor

import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for uptime readings.
 */
@Entity
class WiFiSignalPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Uptime, in seconds.
     */
    val signalStrength: Int?
) : Payload(deviceName, timestamp, data)