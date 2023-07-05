package edu.monash.humanise.smartcity.pcr2

import kotlinx.serialization.Serializable


@Serializable
data class PayloadUplinkRequest(val deviceName: String, val devEUI: String, val data: String)
