package edu.monash.humanise.smartcity.athompresencesensor.payload

import jakarta.persistence.*
import java.time.OffsetDateTime

/**
 * Name for the global ID sequence.
 */
const val SEQUENCE_NAME = "athom_presence_sensor_id_sequence"

/**
 * Presence sensor data entity.
 *
 * Each sensor type has its own entity classes, such as [OccupancyPayload] for occupancy data.
 * This class is the superclass of all sensor data classes, and does not actually exist in the database.
 */
@MappedSuperclass
open class Payload(
    /**
     * Device name of where this datapoint is from. This corresponds to the `esphome.name` attribute in the device's
     * yml file.
     */
    @Column(nullable = false)
    val deviceName: String,
    /**
     * Timestamp of datapoint according to MQTT timestamp, or when the message arrived at the collector if the timestamp is not available.
     */
    @Column(nullable = false)
    val timestamp: OffsetDateTime,
    /**
     * Raw data of the MQTT payload, stored as a string.
     */
    @Column(nullable = false)
    val data: String
) {
    /**
     * A unique number of each sensor datapoint, shared across all sensor entities. This is just a sequence that
     * counts up and does not have any other semantic meaning.
     */
    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    var id: Long = 0
}