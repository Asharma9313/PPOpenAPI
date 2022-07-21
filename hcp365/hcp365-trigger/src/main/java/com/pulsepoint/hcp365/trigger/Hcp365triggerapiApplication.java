package com.pulsepoint.hcp365.trigger;


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
@EntityScan(basePackages = {"com.pulsepoint.commons.audit","com.pulsepoint.commons.hibernate", "com.pulsepoint.hcp365.trigger.modal"})
public class Hcp365triggerapiApplication {
    public static void main(String[] args) {
        SpringApplication.run(Hcp365triggerapiApplication.class, args);
    }
}


