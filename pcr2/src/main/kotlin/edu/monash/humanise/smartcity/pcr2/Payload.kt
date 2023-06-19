package edu.monash.humanise.smartcity.pcr2

import jakarta.persistence.*
import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.time.OffsetDateTime

const val SEQUENCE_NAME = "pcr2_id_sequence"

@Entity
class Payload(
    val deviceName: String,
    val data: String,
    val devEUI: String,
    val ltr: Int,
    val rtl: Int,
    val cpuTemp: Int
) {
    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE_NAME)
    var id: Long = 0
    @Column(updatable = false)
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    @UpdateTimestamp
    lateinit var updatedAt: OffsetDateTime
}
