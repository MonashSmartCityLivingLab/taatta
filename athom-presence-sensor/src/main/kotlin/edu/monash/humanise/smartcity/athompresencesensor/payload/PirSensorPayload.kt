package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Entity
import java.time.OffsetDateTime

@Entity
class PirSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    occupancy: Boolean
): Payload(deviceName, timestamp, data)