package com.gildong.api;

import com.gildong.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class GildongApplication {

    public static void main(String[] args) {
        SpringApplication.run(GildongApplication.class, args);
    }

}
