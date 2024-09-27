package com.zurnov.bitcoin.insights.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Value("${RPC_ADDRESS}")
    private String rpcAddress;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(
                        "http://" + rpcAddress + ":4200",
                        "https://" + rpcAddress + ":4200",
                        "http://localhost:4200",
                        "http://localhost:8000",
                        "https://api.zurnov.com",
                        "https://zurnov.com",
                        "https://www.zurnov.com",
                        "https://explore21.com",
                        "https://www.explore21.com",
                        "https://api.explore21.com")
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*");
    }
}
