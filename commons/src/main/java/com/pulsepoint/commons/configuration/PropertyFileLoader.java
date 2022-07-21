package com.pulsepoint.commons.configuration;

import org.springframework.core.io.PathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;

import static java.util.Arrays.stream;

@Component
public class PropertyFileLoader {
    public static Properties loadProperties(final String filename) {
        final Resource[] possiblePropertiesResources = {
                new PathResource(filename),
        };
        final Resource resource = stream(possiblePropertiesResources)
                .filter(Resource::exists)
                .reduce((previous, current) -> current)
                .get();
        final Properties properties = new Properties();

        try {
            properties.load(resource.getInputStream());
        } catch (final IOException exception) {
            throw new RuntimeException(exception);
        }


        return properties;
    }
}
