package edu.monash.humanise.smartcity.athompresencesensor

import jakarta.persistence.Entity
import java.time.OffsetDateTime

@Entity
class OccupancyPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    occupied: Boolean?
): Payload(deviceName, timestamp, data)