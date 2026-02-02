package com.nugenmail.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web MVC Configuration.
 * Configures Spring MVC settings.
 */
@Configuration
@EnableWebMvc
@ComponentScan(basePackages = "com.nugenmail")
public class WebConfig implements WebMvcConfigurer {
    // Add resource handlers, view resolvers, etc. here if needed
}
