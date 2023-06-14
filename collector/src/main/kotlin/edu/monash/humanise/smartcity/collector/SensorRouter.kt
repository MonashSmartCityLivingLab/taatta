package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

/**
 * Class containing information about a sensor router and method for sending data to logger modules.
 *
 * This class is automatically instantiated as [Router] reads `sensorRouters.json` configuration file.
 */
@Serializable
open class SensorRouter(
        /** Sensor name. */
        val name: String,
        /** A list of logger module hostnames. */
        private val hostnames: Array<String>
) {
    /**
     * Sends received data to all logger modules.
     *
     * @param body payload body as a string
     * @return a list [Result] instance for each logger module, with [ResponseEntity] as success value and
     * [RestClientException] as failure value
     */
    open fun sendData(body: String): List<Result<ResponseEntity<String>>> {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        // Send data to each host
        return hostnames.map { hostname ->
            val url = "$hostname/api/payload"
            val request = HttpEntity(body, headers)
            try {
                Result.success(restTemplate.postForEntity(url, request, String::class.java))
            } catch (e: RestClientException) {
                Result.failure(e)
            }
        }
    }
}
