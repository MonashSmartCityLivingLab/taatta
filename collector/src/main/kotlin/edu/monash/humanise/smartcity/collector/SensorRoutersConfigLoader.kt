package edu.monash.humanise.smartcity.collector

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
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
    private val jsonCoder = Json { ignoreUnknownKeys = true }

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
        val sensorRoutersConfig: SensorRoutersConfig = jsonCoder.decodeFromString(configJson)
        logger.info { "Loaded ESPHome routers: ${sensorRoutersConfig.espHomeModules.map { s -> s.name }}" }
        logger.info { "Loaded LoRa routers: ${sensorRoutersConfig.loraModules.map { s -> s.name }}" }
        return sensorRoutersConfig
    }
}