package edu.monash.humanise.smartcity.collector

import io.github.oshai.KotlinLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class CollectorApplication {
    @Value("\${smart.city.broker.url}")
    private val brokerUrl: String? = null

    @Value("\${spring.application.name}")
    private val appName: String? = null

    @Bean
    fun mqttInputChannel(): MessageChannel {
        return DirectChannel()
    }

    @Bean
    fun inbound(): MessageProducer {
        logger.info { "Connecting to MQTT broker at $brokerUrl" }
        val adapter = MqttPahoMessageDrivenChannelAdapter(brokerUrl,
                appName,
                "application/+/device/+/event/+",
                "application/+/device/+/command/+",
                "+/sensor/+/state" // for smart plug
        )
        adapter.setCompletionTimeout(5000)
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = mqttInputChannel()
        return adapter
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    fun handler(): MessageHandler {
        return MessageHandler { message ->
            val router = Router(message)
            router.route()
        }
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(CollectorApplication::class.java)
            .web(WebApplicationType.NONE)
            .run(*args)
}
