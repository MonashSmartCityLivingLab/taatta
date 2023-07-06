package edu.monash.humanise.smartcity.athompresencesensor

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

/**
 * Name for the global ID sequence.
 */
const val SEQUENCE_NAME = "athom_presence_sensor_id_sequence"

@MappedSuperclass
class Payload(
    /**
     * Device name of where this datapoint is from. his corresponds to the `esphome.name` attribute in the plug's
     * yml file.
     */
    @Column(nullable = false)
    val deviceName: String,
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

    /**
     * Creation date for datapoint.
     */
    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    /**
     * Last modified date for datapoint.
     */
    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: OffsetDateTime
}