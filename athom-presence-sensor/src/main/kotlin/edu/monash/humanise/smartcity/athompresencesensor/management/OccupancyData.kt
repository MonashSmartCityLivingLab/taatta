package edu.monash.humanise.smartcity.athompresencesensor.management

import kotlinx.serialization.Serializable

@Serializable
data class OccupancyData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val occupied: Boolean
)