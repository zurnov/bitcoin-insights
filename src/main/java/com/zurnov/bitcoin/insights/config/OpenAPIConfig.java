package com.zurnov.bitcoin.insights.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${openapi.server.url}")
    private String serverUrl;

    @Bean
    public OpenAPI openAPI() {

        Server server = new Server();
        server.setUrl(serverUrl);

        Contact contact = new Contact();
        contact.setUrl("https://zurnov.com");
        contact.setEmail("mitkozurnov@gmail.com.com");
        contact.setName("Dimitar Zurnov");

        Info info = new Info();
        info.setContact(contact);
        info.setTitle("Bitcoin Insights");
        info.setVersion("1.0.0");
        info.setDescription("Bitcoin blockchain explorer");

        return new OpenAPI().info(info).servers(List.of(server));
    }

}
