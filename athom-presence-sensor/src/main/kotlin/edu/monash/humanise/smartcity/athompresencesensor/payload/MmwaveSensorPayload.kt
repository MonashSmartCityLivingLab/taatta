package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

@Entity
class MmwaveSensorPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    @Column(nullable = false)
    val occupied: Boolean,
): Payload(deviceName, timestamp, data)