package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable

@Serializable
data class SensorModuleConfig(
        val loraModules: List<SensorModule>,
        val espHomeModules: List<SensorModule>
)