package com.kth.cprtraining.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * MapperConfig sets up configuration for object mapping in a Spring Boot application.
 * This configuration allows for easy mapping between different object models.
 */
@Configuration
public class MapperConfig {

    /**
     * Creates a bean for the ModelMapper to be used application-wide.
     * ModelMapper is a library that automates the mapping of objects of one type to another.
     */
    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
