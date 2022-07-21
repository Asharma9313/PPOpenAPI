package com.pulsepoint.commons.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@ConditionalOnProperty(value = "otpConfig", havingValue = "API")
@Configuration
public class HikariConfiguration {
    @Value("${hikari.config-file-path}")
    private String configFilePath;

    @Bean
    public DataSource getDataSource() {
        HikariConfig hikariConfig = new HikariConfig(configFilePath);
        return new HikariDataSource(hikariConfig);
    }
}
