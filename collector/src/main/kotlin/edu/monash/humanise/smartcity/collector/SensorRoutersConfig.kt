package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable

@Serializable
data class SensorRoutersConfig(
        val loraModules: List<SensorRouter>,
        val espHomeModules: List<SensorRouter>
)