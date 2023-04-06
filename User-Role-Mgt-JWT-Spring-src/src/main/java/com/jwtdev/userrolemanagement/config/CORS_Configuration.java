package com.jwtdev.userrolemanagement.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CORS_Configuration {

    private static final String GET ="GET";
    private static final String POST ="POST";
    private static final String DELETE ="DELETE";
    private static final String PUT ="PUT";

    public WebMvcConfigurer coRS_config(){

        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                WebMvcConfigurer.super.addCorsMappings(registry);
                registry.addMapping("/**"). allowedMethods(GET,PUT,POST,DELETE)
                        .allowedHeaders("*")
                        .allowedOriginPatterns("*")
                        .allowCredentials(true);

            }
        };
    }

    //WebMvcConfigurer.super.addCorsMappings(registry);
}
