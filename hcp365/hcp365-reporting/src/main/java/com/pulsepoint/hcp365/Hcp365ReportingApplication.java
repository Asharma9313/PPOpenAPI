package com.pulsepoint.hcp365;


import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(basePackages = {"com.pulsepoint"})
@EntityScan(basePackages = {"com.pulsepoint.commons.audit", "com.pulsepoint.commons.hibernate", "com.pulsepoint.hcp365.modal"})
@Log4j2
public class Hcp365ReportingApplication {


    public static void main(String[] args) {
        logEnvironmentInfo();
        SpringApplication.run(Hcp365ReportingApplication.class, args);
    }

    private static void logEnvironmentInfo() {
        log.info(
                "Environment:\n" +
                        " APP_ENV : {}\n" +
                        " APP_CONFIG_FILE_LOCATION : {}\n" +
                        " APP_CONFIG_SERVER : {}\n" +
                        " APP_API_HOST_NAME : {}",
                System.getenv("APP_ENV"),
                System.getenv("APP_CONFIG_FILE_LOCATION"),
                System.getenv("APP_CONFIG_SERVER"),
                System.getenv("APP_API_HOST_NAME"));
    }
}


