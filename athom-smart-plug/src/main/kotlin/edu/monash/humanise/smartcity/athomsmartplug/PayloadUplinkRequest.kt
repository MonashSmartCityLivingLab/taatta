package edu.monash.humanise.smartcity.athomsmartplug

/**
 * Payload data send from the collector.
 */
data class PayloadUplinkRequest(
        /**
         * Device name. This corresponds to the `esphome.name` attribute in the plug's yml file.
         */
        val deviceName: String = "",
        /**
         * Sensor name. See the module README for a list of known sensor name.
         */
        val sensor: String = "",
        /**
         * Payload data.
         */
        val data: String = ""
)
