package edu.monash.humanise.smartcity.pcr2

import org.springframework.data.jpa.repository.JpaRepository

interface PayloadRepository : JpaRepository<Payload, Long>
