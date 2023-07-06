package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

typealias ResponseResult = Result<ResponseEntity<String>>

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
    @Transient
    private val restTemplate = RestTemplate()
    @Transient
    private val headers = HttpHeaders()
    @Transient
    private val requestQueue = ArrayDeque<Pair<String, HttpEntity<String>>>()

    open fun addToQueue(body: String) {
        headers.contentType = MediaType.APPLICATION_JSON
        hostnames.forEach { hostname ->
            val url = "$hostname/api/payload"
            val request = HttpEntity(body, headers)
            requestQueue.add(url to request)
        }
    }

    /**
     * Sends received data to all logger modules.
     *
     * @return a list [Result] instance for each logger module, with [ResponseEntity] as success value and
     * [RestClientException] as failure value
     */
    open fun sendData(): List<ResponseResult> {
        val results: MutableList<ResponseResult> = mutableListOf()
        while (!requestQueue.isEmpty()) {
            val (url, request) = requestQueue.removeFirst()
            val result: ResponseResult = try {
                Result.success(restTemplate.postForEntity(url, request, String::class.java))
            } catch (e: RestClientException) {
                // if there's failure, re-add request to queue
                requestQueue.add(url to request)
                Result.failure(e)
            }
            results.add(result)
        }
        return results
    }
}
