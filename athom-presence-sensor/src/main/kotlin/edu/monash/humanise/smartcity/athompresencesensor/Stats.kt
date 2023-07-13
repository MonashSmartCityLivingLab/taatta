package edu.monash.humanise.smartcity.athompresencesensor

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

/**
 * Component which keeps track of number of requests, and logs them periodically.
 */
@Component
class Stats {
    private val payloadCount: MutableMap<String, Int> = mutableMapOf()
    private var previousTime = OffsetDateTime.now(ZoneOffset.UTC)

    /**
     * Write statistics periodically to log.
     */
    @Scheduled(cron = "0 * * * * *") // print log every minute
    fun printStats() {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val stringBuilder = StringBuilder("Number of payload requests for athom-presence-sensor by device from $previousTime to $now:\n")

        for ((sensor, count) in payloadCount.toSortedMap()) {
            stringBuilder.append("$sensor: $count\n")
        }
        stringBuilder.append("Total: ${payloadCount.values.sum()} from ${payloadCount.count()} devices")

        logger.info { stringBuilder.toString() }

        payloadCount.clear()
        previousTime = now
    }

    fun incrementCounter(sensor: String) {
        val oldCount = payloadCount[sensor] ?: 0
        payloadCount[sensor] = oldCount + 1
    }
}