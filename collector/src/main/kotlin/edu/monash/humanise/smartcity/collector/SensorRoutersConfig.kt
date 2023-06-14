package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable

/**
 * Class containing a list of sensor routers for both LoRaWAN and ESPHome logging modules.
 */
@Serializable
data class SensorRoutersConfig(
        val loraModules: List<SensorRouter>,
        val espHomeModules: List<SensorRouter>
)