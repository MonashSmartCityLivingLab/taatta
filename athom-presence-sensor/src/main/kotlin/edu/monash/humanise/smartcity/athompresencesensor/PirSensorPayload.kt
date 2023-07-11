package edu.monash.humanise.smartcity.athompresencesensor

import jakarta.persistence.Entity
import java.time.OffsetDateTime

@Entity
class PirSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    motionDetected: Boolean?
): Payload(deviceName, timestamp, data)