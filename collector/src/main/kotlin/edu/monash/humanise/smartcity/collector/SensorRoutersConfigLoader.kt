package edu.monash.humanise.smartcity.collector

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import java.io.File

private val logger = KotlinLogging.logger {}

/**
 * Component to load the router configuration file.
 */
@Component
class SensorRoutersConfigLoader {
    /**
     * JSON encoder/decoder.
     */
    private val jsonCoder = jacksonObjectMapper()

    /**
     * Load router configuration from disk.
     *
     * It will try to read from a file located in `TAATTA_SENSOR_ROUTERS` environment variable. If
     * `TAATTA_SENSOR_ROUTERS` is not available, it will try to read `sensorRouters.json` in current working directory.
     * You should use the environment variable if possible, as the current working directory can be unpredictable at
     * times.
     */
    fun loadConfig(): SensorRoutersConfig {
        val configPath = System.getenv("TAATTA_SENSOR_ROUTERS") ?: "sensorRouters.json"
        val configFile = File(configPath)
        logger.info { "Reading sensor router config from ${configFile.absolutePath}" }
        val configJson = configFile.readText()
        val sensorRoutersConfig: SensorRoutersConfig = jsonCoder.readValue(configJson)
        logger.info { "Loaded ESPHome routers: ${sensorRoutersConfig.espHomeModules.map { s -> s.name }}" }
        logger.info { "Loaded LoRa routers: ${sensorRoutersConfig.loraModules.map { s -> s.name }}" }
        return sensorRoutersConfig
    }
}