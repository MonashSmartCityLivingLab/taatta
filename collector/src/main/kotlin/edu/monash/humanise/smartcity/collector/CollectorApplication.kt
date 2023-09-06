package edu.monash.humanise.smartcity.collector

import io.github.oshai.kotlinlogging.KotlinLogging
import org.eclipse.paho.client.mqttv3.MqttConnectOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.core.MessageProducer
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import java.util.*

private val logger = KotlinLogging.logger {}

@SpringBootApplication
class CollectorApplication(private val router: Router) {
    /**
     * Broker URL and port, specified using `TAATTA_MOSQUITTO_HOST` and `TAATTA_MOSQUITTO_PORT` environment variables
     * respectively.
     */
    @Value("\${smart-city.broker-url}")
    private val brokerUrl: String? = null

    /**
     * MQTT broker username, specified using `TAATTA_MOSQUITTO_USER` environment variable. Defaults to `null` if the
     * environment variable is not present.
     */
    @Value("\${smart-city.broker-username}")
    private val brokerUsername: String? = null

    /**
     * MQTT broker password, specified using `TAATTA_MOSQUITTO_USER` environment variable. Defaults to `null` if the
     * environment variable is not present.
     */
    @Value("\${smart-city.broker-password}")
    private val brokerPassword: String? = null

    /**
     * Application name. Always set to `collector` and is never `null` after initialisation.
     */
    @Value("\${spring.application.name}")
    private val appName: String? = null

    /**
     * Sets MQTT message channel.
     */
    @Bean
    fun mqttInputChannel(): MessageChannel {
        return DirectChannel()
    }

    /**
     * Initialise MQTT adapter.
     */
    @Bean
    fun inbound(): MessageProducer {
        // clientId must be unique across the broker. otherwise, we'll get broken pipes, EOFException and other weird behaviours.
        // this can happen if there are 2 collector instances running at the same time (e.g. a prod instance and dev instance)
        // source: https://stackoverflow.com/a/61161172
        val randId = UUID.randomUUID()
        val clientId = "$appName-$randId"

        val topics = arrayOf(
            // for lorawan
            "application/+/device/+/event/+",
            "application/+/device/+/command/+",
            // for esphome
            "+/sensor/+/state",
            "+/binary_sensor/+/state",
            "+/status", // sensor status
            "+/debug",
            "+/switch/+"
        )

        logger.info { "Connecting to MQTT broker at $brokerUrl with client ID $clientId" }
        val adapter = if (brokerUsername != null && brokerPassword != null) {
            val connectOptions = MqttConnectOptions()
            connectOptions.userName = brokerUsername
            connectOptions.password = brokerPassword.toCharArray()
            val clientFactory = DefaultMqttPahoClientFactory()
            clientFactory.connectionOptions = connectOptions
            MqttPahoMessageDrivenChannelAdapter(
                brokerUrl,
                clientId,
                clientFactory,
                *topics
            )
        } else {
            MqttPahoMessageDrivenChannelAdapter(
                brokerUrl,
                clientId,
                *topics
            )
        }
        adapter.setConverter(DefaultPahoMessageConverter())
        adapter.setQos(1)
        adapter.outputChannel = mqttInputChannel()
        return adapter
    }

    /**
     * Handles incoming message.
     */
    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    fun handler(): MessageHandler {
        return MessageHandler { message ->
            router.route(message)
        }
    }
}


fun main(args: Array<String>) {
    SpringApplicationBuilder(CollectorApplication::class.java)
        .web(WebApplicationType.NONE)
        .run(*args)
}
