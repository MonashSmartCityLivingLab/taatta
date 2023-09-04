package edu.monash.smartcity.idledevicemanagement.model

import kotlinx.serialization.Serializable

@Serializable
data class OccupancyData(
    val timestampMilliseconds: Long,
    val deviceName: String,
    val occupied: Boolean
)