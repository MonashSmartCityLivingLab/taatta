package edu.monash.humanise.smartcity.collector

import org.json.JSONObject
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate

open class SensorModule(
        val sensorName: String,
        val port: Int,
        val hostname: String
) {
    fun sendData(body: JSONObject, isDocker: Boolean): ResponseEntity<String> {
        val restTemplate = RestTemplate()
        val headers = HttpHeaders()
        headers.contentType = MediaType.APPLICATION_JSON
        val url = "http://$hostname:$port/api/payload"
        val request = HttpEntity(body, headers)
        return restTemplate.postForEntity(url, request, String::class.java)
    }
}
