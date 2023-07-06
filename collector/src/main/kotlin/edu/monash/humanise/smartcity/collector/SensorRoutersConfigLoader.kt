package edu.monash.humanise.smartcity.collector

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Component
import java.io.File

private val logger = KotlinLogging.logger {}

@Component
class SensorRoutersConfigLoader {
    private val jsonCoder = Json { ignoreUnknownKeys = true }

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