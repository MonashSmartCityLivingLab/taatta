package edu.monash.humanise.smartcity.athompresencesensor

import edu.monash.humanise.smartcity.athompresencesensor.payload.Payload
import org.springframework.data.jpa.repository.JpaRepository

interface PayloadRepository: JpaRepository<Payload, Long>