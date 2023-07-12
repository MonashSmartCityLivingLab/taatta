package edu.monash.humanise.smartcity.athomsmartplug

/**
 * Decoder for ESPHome data.
 */
class Decoder {
    companion object {
        /**
         * Decode data to [Int].
         * @throws DecoderException if the number cannot be parsed
         */
        fun decodeIntegerSensorState(data: String): Int = try {
            data.toInt()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid integer value: $data")
        }

        /**
         * Decode data to [Double].
         * @throws DecoderException if the number cannot be parsed
         */
        fun decodeFloatSensorState(data: String): Double = try {
            data.toDouble()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid float value: $data")
        }

        /**
         * Decode binary data to [Boolean]. Allowed values are `ON` and `OFF` (case-insensitive).
         * @throws DecoderException if the value cannot be parsed
         */
        fun decodeBinarySensorState(data: String): Boolean = when (data.uppercase()) {
            "ON" -> true
            "OFF" -> false
            else -> {
                throw DecoderException("Invalid binary value: $data. Accepted values are ON or OFF")
            }
        }

        /**
         * Decode data to [Long].
         * @throws DecoderException if the number cannot be parsed
         */
        fun decodeLongSensorState(data: String): Long = try {
            data.toLong()
        } catch (e: NumberFormatException) {
            throw DecoderException("Invalid long value: $data")
        }
    }
}