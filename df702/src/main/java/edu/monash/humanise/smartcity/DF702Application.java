package edu.monash.humanise.smartcity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableConfigurationProperties
//@EntityScan(basePackages = {"edu.monash.humanise.smartcity"})
public class DF702Application {
    private static ConfigurableApplicationContext applicationContext;

    public static void main(String[] args) {
        SpringApplication.run(DF702Application.class, args);
    }
}
