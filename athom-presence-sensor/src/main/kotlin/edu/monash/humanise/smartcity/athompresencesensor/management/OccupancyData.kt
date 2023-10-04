package edu.monash.humanise.smartcity.athompresencesensor.management




data class OccupancyData(
    val timestampMilliseconds: Long,
    val sensorName: String,
    val occupied: Boolean
)