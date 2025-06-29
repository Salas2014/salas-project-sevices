package com.salas.configuration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class ConfigurationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationServiceApplication.class, args);
    }

}
