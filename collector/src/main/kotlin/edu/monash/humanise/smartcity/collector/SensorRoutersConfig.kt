package edu.monash.humanise.smartcity.collector



/**
 * Class containing a list of sensor routers for both LoRaWAN and ESPHome logging modules.
 */

data class SensorRoutersConfig(
    val loraModules: List<SensorRouter>,
    val espHomeModules: List<SensorRouter>
)