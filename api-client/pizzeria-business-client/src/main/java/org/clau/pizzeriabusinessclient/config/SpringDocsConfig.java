package org.clau.pizzeriabusinessclient.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pizzeria Business API", version = "v1"))
@SecurityScheme(
		name = "OAUTH2",
		type = SecuritySchemeType.OAUTH2
)
public class SpringDocsConfig {
}
