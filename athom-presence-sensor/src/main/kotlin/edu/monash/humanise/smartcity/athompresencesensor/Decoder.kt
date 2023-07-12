package edu.monash.humanise.smartcity.athompresencesensor

class Decoder {
    companion object {
        fun decodeIntegerSensorState(data: String): Int = try {
            data.toInt()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid integer value: $data")
        }

        fun decodeFloatSensorState(data: String): Double = try {
            data.toDouble()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid float value: $data")
        }

        fun decodeBinarySensorState(data: String): Boolean = when (data.uppercase()) {
            "ON" -> true
            "OFF" -> false
            else -> {
                throw DecoderException("Invalid binary value: $data. Accepted values are ON or OFF")
            }
        }

        fun decodeLongSensorState(data: String): Long = try {
            data.toLong()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid long value: $data")
        }
    }
}