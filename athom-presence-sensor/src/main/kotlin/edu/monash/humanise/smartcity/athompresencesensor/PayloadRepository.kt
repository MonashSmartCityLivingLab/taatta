package edu.monash.humanise.smartcity.athompresencesensor

import org.springframework.data.jpa.repository.JpaRepository

interface PayloadRepository: JpaRepository<Payload, Long>