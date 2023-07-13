package edu.monash.humanise.smartcity.athomsmartplug

import edu.monash.humanise.smartcity.athomsmartplug.payload.Payload
import org.springframework.data.jpa.repository.JpaRepository

/**
 * Smart plug sensor data repository.
 */
interface PayloadRepository : JpaRepository<Payload, Long>