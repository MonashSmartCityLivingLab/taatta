package edu.monash.humanise.smartcity.pcr2


data class PayloadUplinkRequest(val deviceName: String = "", val devEUI: String = "", val data: String = "")
