package com.pulsepoint.commons.configuration;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@Log4j2
public class RestTemplateConfiguration {

    @Bean
    RestTemplate getDefaultRestTemplate() {
        log.debug("initializing rest template..");
        return new RestTemplate();
    }
}