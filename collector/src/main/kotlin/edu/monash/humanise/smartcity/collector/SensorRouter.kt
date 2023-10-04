package edu.monash.humanise.smartcity.collector


import com.fasterxml.jackson.annotation.JsonIgnore
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
 */

open class SensorRouter(
    /** Sensor name. */
    val name: String,
    /** A list of logger module hostnames. */
    private val hostnames: Array<String>
) {
    /**
     * HTTP client for this router.
     */
    @JsonIgnore
    private val restTemplate = RestTemplate()

    /**
     * HTTP headers for all requests.
     */
    @JsonIgnore
    private val headers = initHttpHeaders()

    /**
     * A queue of all requests to be sent later.
     */
    @JsonIgnore
    private val requestQueue = ArrayDeque<RequestPair>()

    /**
     * Sends received data to all logger modules.
     *
     * If any of the requests failed, it will be put to a queue. You can retry sending the failed requests by calling
     * [retryFailedRequests].
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

    /**
     * Try to re-send any failed requests.
     *
     * If any requests still failed after calling this method, it will pushed back to a queue, so it can be sent at a
     * later time.
     */
    open fun retryFailedRequests(): List<ResponseResult>  {
        val failedAgainList: MutableList<RequestPair> = mutableListOf()
        val results: MutableList<ResponseResult> = mutableListOf()
        while (!requestQueue.isEmpty()) {
            val (url, request) = requestQueue.removeFirst()
            results.add(
                try {
                    Result.success(restTemplate.postForEntity(url, request, String::class.java))
                } catch (e: RestClientException) {
                    failedAgainList.add(url to request)
                    Result.failure(e)
                }
            )
        }
        requestQueue.addAll(failedAgainList)
        return results
    }

    companion object {
        /**
         * Initializer for the HTTP header.
         *
         * @return an instance of [HttpHeaders] with `contentType` set to `application/json`
         */
        private fun initHttpHeaders(): HttpHeaders {
            val httpHeaders = HttpHeaders()
            httpHeaders.contentType = MediaType.APPLICATION_JSON
            return httpHeaders
        }
    }
}
