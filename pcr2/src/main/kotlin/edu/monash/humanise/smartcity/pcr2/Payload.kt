package edu.monash.humanise.smartcity.pcr2

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

const val SEQUENCE_NAME = "pcr2_id_sequence"

/**
 * Database entity for PCR2 payload data.
 *
 * See [the payload description](https://docs.parametric-analytics.com/pcr2/manuals/lora_payload_v3/) to learn more.
 */
@Entity
class Payload(
    @Column(nullable = false)
    val deviceName: String,
    /**
     * Raw payload data, encoded as string in base64.
     */
    @Column(nullable = false)
    val data: String,
    @Column(nullable = false)
    val devEUI: String,
    /**
     * Left-to-right counter. Allowed values: 0 to 65535
     */
    @Column(nullable = false)
    val ltr: Int,
    /**
     * Right-to-left counter. Allowed values: 0 to 65535
     */
    @Column(nullable = false)
    val rtl: Int,
    /**
     * CPU temperature. Allowed values: -3276.8°C to 3276.7°C
     */
    @Column(nullable = false)
    val cpuTemp: Double
) {
    /**
     * A unique number of each sensor datapoint. This is just a sequence that counts up and does not have any other
     * semantic meaning.
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
