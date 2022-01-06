package com.cybertek.config;


import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info; // import from correct libraray
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenApi(){

        SecurityScheme securitySchemeItem = new SecurityScheme();
        securitySchemeItem.setType(SecurityScheme.Type.HTTP);
        securitySchemeItem.setScheme("bearer"); // when we sent token, it is auto initilized with "Bearer" name...
        securitySchemeItem.setBearerFormat("JWT"); // token type - jwt
        securitySchemeItem.setIn(SecurityScheme.In.HEADER);// where is this token
        securitySchemeItem.setName("Authorization");// what is your key in header
        Info infoVersion = new Info().title("Cybertek Ticketing Application").version("snapshot");// info object
        SecurityRequirement securityItem = new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read","write")); // read access, write access

        return new OpenAPI()
                .components(new Components()
                .addSecuritySchemes("bearer-jwt",securitySchemeItem))
                .info(infoVersion)
                .addSecurityItem(securityItem);
    }

}
