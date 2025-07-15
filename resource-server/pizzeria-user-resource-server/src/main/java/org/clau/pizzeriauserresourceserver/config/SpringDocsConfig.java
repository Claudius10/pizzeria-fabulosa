package org.clau.pizzeriauserresourceserver.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pizzeria User API", version = "v1"), servers = {@Server(url = "http://127.0.0.1:8080")})
public class SpringDocsConfig {
}
