package edu.monash.humanise.smartcity.athomsmartplug

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.SequenceGenerator
import javax.persistence.Transient

const val SEQUENCE_NAME = "athom_smart_plug_id_sequence"

@Entity
class PlugData(
        @Id
        @SequenceGenerator(name = SEQUENCE_NAME, sequenceName = SEQUENCE_NAME)
        @GeneratedValue(strategy = GenerationType.AUTO, generator = SEQUENCE_NAME)
        private val id: Int,
        @Column(updatable = false)
        @CreationTimestamp
        private val createdAt: LocalDateTime,
        @UpdateTimestamp
        private val updatedAt: LocalDateTime,

        val uptime: Double?,
        val voltage: Double?,
        val current: Double?,
        val power: Double?,
        val energyConsumption: Double?,
        val dailyEnergyConsumption: Double?,
)