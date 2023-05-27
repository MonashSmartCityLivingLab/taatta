package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.data.jpa.repository.JpaRepository

interface PayloadRepository : JpaRepository<Payload, Int>