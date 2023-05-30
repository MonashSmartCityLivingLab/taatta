package edu.monash.humanise.smartcity.collector;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;

@Slf4j
@SpringBootApplication
public class CollectorApplication {
    @Value("${smart.city.broker.url}")
    private String brokerUrl;
    @Value("${spring.application.name}")
    private String appName;

    public static void main(String[] args) {
        new SpringApplicationBuilder(CollectorApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

//    @Bean
//    public IntegrationFlow mqttInbound() {
//        return IntegrationFlows.from(
//                new MqttPahoMessageDrivenChannelAdapter("tcp://10.130.1.239:1883",
//                        "SmartCityCollector", "application/+/device/+/event/+", "application/+/device/+/command/+"))
//                .handle(m -> {
//                String msg = m.getPayload().toString();
//                Router router = new Router(msg);
//                router.route();
//                })
//                .get();
//    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        log.info("Connecting MQTT broker url: {}", brokerUrl);
        MqttPahoMessageDrivenChannelAdapter adapter =
                new MqttPahoMessageDrivenChannelAdapter(brokerUrl,
                        appName,
                        "application/+/device/+/event/+",
                        "application/+/device/+/command/+",
                        "+/sensor/+/state" // for smart plug
                );
        adapter.setCompletionTimeout(5000);
        adapter.setConverter(new DefaultPahoMessageConverter());
        adapter.setQos(1);
        adapter.setOutputChannel(mqttInputChannel());
        return adapter;
    }

    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
                Router router = new Router(message);
                router.route();
            }
        };
    }

}
