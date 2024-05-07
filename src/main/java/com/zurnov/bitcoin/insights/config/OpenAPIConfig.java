package com.zurnov.bitcoin.insights.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI openAPI() {

        Contact contact = new Contact();
        contact.setUrl("https://zurnov.com");
        contact.setEmail("mitkozurnov@gmail.com.com");
        contact.setName("Dimitar Zurnov");

        Info info = new Info();
        info.setContact(contact);
        info.setTitle("Bitcoin Insights");
        info.setVersion("1.0.0");
        info.setDescription("Bitcoin blockchain explorer");

        return new OpenAPI().info(info);
    }

}
