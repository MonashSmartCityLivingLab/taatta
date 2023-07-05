package edu.monash.humanise.smartcity.pcr2

import io.github.oshai.KotlinLogging
import java.lang.IndexOutOfBoundsException
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

private val logger = KotlinLogging.logger {}

class Decoder private constructor(val encoded: String, val ltr: Int, val rtl: Int, val cpuTemp: Double) {
    companion object {
        @OptIn(ExperimentalEncodingApi::class, ExperimentalUnsignedTypes::class)
        fun decode(encoded: String): Decoder {
            try {
                val byteArray = Base64.decode(encoded).toUByteArray()

                // https://docs.parametric-analytics.com/pcr2/manuals/lora_payload_v3
                val type = byteArray[0]
                if (type == 0x0a.toUByte()) {
                    // decode ELSYS payload
                    val ltr = (byteArray[1].toInt() shl 8) or (byteArray[2].toInt())
                    val rtl = (byteArray[4].toInt() shl 8) or (byteArray[5].toInt())
                    val cpuTempInt = (byteArray[7].toInt() shl 8) or (byteArray[8].toInt())
                    val cpuTemp = cpuTempInt.toDouble() / 10
                    logger.info { "LTR: $ltr, RTL: $rtl, CPU temp: $cpuTemp" }
                    return Decoder(encoded, ltr, rtl, cpuTemp)
                } else if (type == 0.toUByte() && byteArray[1] == 0x66.toUByte()) {
                    // decode LPP payload
                    logger.info { "CPU temp ${byteArray[8]}" }
                    TODO("Payload type LPP not supported yet: need to figure out weird temperature data size")
                } else if (type == 0xbe.toUByte() && byteArray[1] == 0x01.toUByte() && byteArray[2] == 0x03.toUByte()) {
                    // decode extended payload V3
                    val ltr = (byteArray[3].toInt() shl 8) or (byteArray[4].toInt())
                    val rtl = (byteArray[5].toInt() shl 8) or (byteArray[6].toInt())
                    val cpuTempInt = (byteArray[14].toInt() shl 8) or (byteArray[15].toInt())
                    val cpuTemp = cpuTempInt.toDouble() / 10
                    logger.info { "LTR: $ltr, RTL: $rtl, CPU temp: $cpuTemp" }
                    return Decoder(encoded, ltr, rtl, cpuTemp)
                } else {
                    throw DecoderException("Unknown payload type: $type")
                }
            } catch (e: Exception) {
                when (e) {
                    is IllegalArgumentException, is IndexOutOfBoundsException -> {
                        throw DecoderException("Cannot decode payload with data $encoded", e)
                    }
                    else -> {
                        throw e
                    }
                }
            }
        }
    }
}