package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Smart plug sensor data repository.
 */
interface PayloadRepository : JpaRepository<Payload, Int>