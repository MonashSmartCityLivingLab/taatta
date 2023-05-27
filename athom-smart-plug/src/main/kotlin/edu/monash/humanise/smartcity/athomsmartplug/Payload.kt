package edu.monash.humanise.smartcity.athomsmartplug

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.MappedSuperclass
import javax.persistence.SequenceGenerator

const val SEQUENCE_NAME = "athom_smart_plug_id_sequence"

@MappedSuperclass
open class Payload(
    @Id
    @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
    @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQUENCE_NAME)
    val id: Int = 0,
    val deviceName: String,
    val data: String
) {
    @Column(updatable = false)
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime
    @UpdateTimestamp
    lateinit var updatedAt: OffsetDateTime
}