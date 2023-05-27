package edu.monash.humanise.smartcity.athomsmartplug

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.OffsetDateTime
import javax.persistence.*

@MappedSuperclass
open class Payload(
        @Id
        val id: Long = 0,
        val deviceName: String,
        val data: String
) {
    @Column(updatable = false)
    @CreationTimestamp
    lateinit var createdAt: OffsetDateTime

    @UpdateTimestamp
    lateinit var updatedAt: OffsetDateTime
}