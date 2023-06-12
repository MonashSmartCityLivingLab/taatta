package edu.monash.humanise.smartcity.collector

import kotlinx.serialization.Serializable
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate

@Serializable
open class SensorRouter(
        val name: String,
        private val hostnames: Array<String>
) {
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
