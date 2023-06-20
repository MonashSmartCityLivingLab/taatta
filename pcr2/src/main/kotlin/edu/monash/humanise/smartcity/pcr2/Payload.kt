package edu.monash.humanise.smartcity.pcr2

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime

const val SEQUENCE_NAME = "pcr2_id_sequence"

@Entity
class Payload(
    @Column(nullable = false)
    val deviceName: String,
    @Column(nullable = false)
    val data: String,
    @Column(nullable = false)
    val devEUI: String,
    @Column(nullable = false)
    val ltr: Int,
    @Column(nullable = false)
    val rtl: Int,
    @Column(nullable = false)
    val cpuTemp: Double
) {
    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    var id: Long = 0

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    @UpdateTimestamp
    @Column(nullable = false)
    lateinit var updatedAt: OffsetDateTime
}
