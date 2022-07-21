package com.pulsepoint.configmanager;

import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
@Log4j2
public class ConfigurationManagerApplication {

    public static void main(String[] args) {
        logEnvironmentInfo();
        SpringApplication.run(ConfigurationManagerApplication.class, args);
    }

    private static void logEnvironmentInfo() {
        log.info(
                "Environment:\n" +
                        " APP_ENV : {}\n" +
                        " APP_CONFIG_FILE_LOCATION : {}\n",
                System.getenv("APP_ENV"),
                System.getenv("APP_CONFIG_FILE_LOCATION")
        );
    }
}
