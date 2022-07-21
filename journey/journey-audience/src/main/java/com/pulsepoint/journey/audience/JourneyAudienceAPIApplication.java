package com.pulsepoint.journey.audience;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAutoConfiguration
@EnableAspectJAutoProxy(proxyTargetClass=true)
@ComponentScan(basePackages = {"com.pulsepoint"})
@EntityScan(basePackages = {"com.pulsepoint.commons.audit","com.pulsepoint.commons.hibernate", "com.pulsepoint.journey.audience.modal"})
public class JourneyAudienceAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(JourneyAudienceAPIApplication.class, args);
    }
}


