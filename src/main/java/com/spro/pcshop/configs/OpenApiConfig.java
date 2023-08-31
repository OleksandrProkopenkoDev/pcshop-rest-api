package com.spro.pcshop.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "PC-shop",
                        url = "https://quizicalpro.netlify.app/"
                ),
                description = "OpenApi documentation for PC-shop REST-api",
                title = "OpenApi specifications - PC-shop",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
