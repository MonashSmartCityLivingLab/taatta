package edu.monash.humanise.smartcity.athomsmartplug

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.OffsetDateTime
import java.time.ZoneOffset

private val logger = KotlinLogging.logger {}

@Component
class Stats {
    private val payloadCount: MutableMap<String, Int> = LinkedHashMap()
    private var previousTime = OffsetDateTime.now(ZoneOffset.UTC)

    @Scheduled(cron = "0 * * * * *") // print log every minute
    fun printStats() {
        val now = OffsetDateTime.now(ZoneOffset.UTC)
        val stringBuilder = StringBuilder("Number of payload requests for athom-smart-plug by device from $previousTime to $now:\n")

        for ((sensor, count) in payloadCount.toSortedMap()) {
            stringBuilder.append("$sensor: $count\n")
        }
        stringBuilder.append("Total: ${payloadCount.values.sum()} from ${payloadCount.count()} sensors")

        logger.info { stringBuilder.toString() }

        payloadCount.clear()
        previousTime = now
    }

    fun incrementCounter(sensor: String) {
        val oldCount = payloadCount[sensor] ?: 0
        payloadCount[sensor] = oldCount + 1
    }
}