package edu.monash.humanise.smartcity.athomsmartplug

import jakarta.persistence.*
import java.time.OffsetDateTime

/**
 * Name for the global ID sequence.
 */
const val SEQUENCE_NAME = "athom_smart_plug_id_sequence"

/**
 * Smart plug sensor data entity.
 *
 * Each sensor type has its own entity classes, such as [TotalEnergyConsumptionPayload] for total energy consumption
 * data. This class is the superclass of all sensor data classes, and does not actually exist in the database.
 */
@MappedSuperclass
open class Payload(
    /**
     * Device name of where this datapoint is from. his corresponds to the `esphome.name` attribute in the plug's
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