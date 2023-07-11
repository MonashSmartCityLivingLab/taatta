package edu.monash.humanise.smartcity.athompresencesensor

import jakarta.persistence.Entity
import java.time.OffsetDateTime

@Entity
class MmWaveSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    motionDetected: Boolean?
): Payload(deviceName, timestamp, data)