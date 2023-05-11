package edu.monash.humanise.smartcity.athomsmartplug

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime


// class created partly with chatgpt help
@Component
class PlugDataFetchClient {
    private val restTemplate = RestTemplate()
    @Scheduled(fixedRate = 10000)
    fun makeRequest() {
        println("Scheduled task running: ${LocalDateTime.now()}")
        val response: String? = restTemplate.getForObject("https://example.com", String::class.java)
        println(response)
    }
}