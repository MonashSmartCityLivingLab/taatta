package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


/**
 * Spring application for the Athom Smart Plug V2 logging module.
 */
@SpringBootApplication
class AthomSmartPlugApplication

fun main(args: Array<String>) {
    runApplication<AthomSmartPlugApplication>(*args)
}
