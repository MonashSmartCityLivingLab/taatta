package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.Column
import jakarta.persistence.Entity
import java.time.OffsetDateTime

/**
 * Sensor datapoint entity for occupancy readings from mmWave and PIR sensors.
 *
 * See the [yaml file](https://github.com/athom-tech/athom-configs/blob/2bf3567ddb25fc35174efd693a1cc5afa8626a1b/athom-presence-sensor.yaml#L116)
 * to learn more how occupancy is determined for this entity. For raw occupancy values from the individual sensors, see
 * [MmwaveSensorPayload] and [PirSensorPayload]
 */
@Entity
class OccupancyPayload(
    deviceName: String,
    timestamp: OffsetDateTime,
    data: String,
    /**
     * Occupancy detection from combination of mmWave and PIR sensors .
     */
    @Column(nullable = false)
    val occupied: Boolean
): Payload(deviceName, timestamp, data)