package com.pulsepoint.hcp365;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

@SpringBootTest
@EnableAutoConfiguration
@AutoConfigurationPackage
//@Import({ HikariConfiguration.class, ModalMapperConfiguration.class, PropertyFileLoader.class })
@ComponentScan(basePackages = {"com.pulsepoint"})
@EntityScan(basePackages = {"com.pulsepoint.commons.audit", "com.pulsepoint.commons.hibernate", "com.pulsepoint.hcp365.modal"})
class Hcp365ReportingApplicationTests {

    @Test
    void contextLoads() {
    }

}
