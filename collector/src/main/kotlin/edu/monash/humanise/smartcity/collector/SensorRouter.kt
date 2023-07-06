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
private typealias RequestPair = Pair<String, HttpEntity<String>>

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
    private val headers = initHttpHeaders()
    @Transient
    private val requestQueue = ArrayDeque<RequestPair>()

    /**
     * Sends received data to all logger modules.
     *
     * @param body payload body as a string
     * @return a list [Result] instance for each logger module, with [ResponseEntity] as success value and
     * [RestClientException] as failure value
     */
    open fun sendData(body: String): List<ResponseResult> {
        // Send data to each host
        return hostnames.map { hostname ->
            val url = "$hostname/api/payload"
            val request = HttpEntity(body, headers)
            try {
                Result.success(restTemplate.postForEntity(url, request, String::class.java))
            } catch (e: RestClientException) {
                requestQueue.add(url to request)
                Result.failure(e)
            }
        }
    }

    open fun retryFailedRequests(): List<ResponseResult>  {
        val failedAgainList: MutableList<RequestPair> = mutableListOf()
        val results: MutableList<ResponseResult> = mutableListOf()
        while (!requestQueue.isEmpty()) {
            val (url, request) = requestQueue.removeFirst()
            results.addAll(hostnames.map { hostname ->
                try {
                    Result.success(restTemplate.postForEntity(url, request, String::class.java))
                } catch (e: RestClientException) {
                    failedAgainList.add(url to request)
                    Result.failure(e)
                }
            })
        }
        requestQueue.addAll(failedAgainList)
        return results
    }

    companion object {
        private fun initHttpHeaders(): HttpHeaders {
            val httpHeaders = HttpHeaders()
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            return httpHeaders
        }
    }
}
