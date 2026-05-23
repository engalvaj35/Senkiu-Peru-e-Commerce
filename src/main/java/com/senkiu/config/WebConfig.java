package com.senkiu.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(
            ResourceHandlerRegistry registry
    ) {

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(
                        "file:src/main/resources/static/uploads/"
                );
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}