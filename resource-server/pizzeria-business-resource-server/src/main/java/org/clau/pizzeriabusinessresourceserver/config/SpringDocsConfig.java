package org.clau.pizzeriabusinessresourceserver.config;


import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pizzeria Business API", version = "v1"), servers = {@Server(url = "http://127.0.0.1:8080")})
//@SecurityScheme(
//		name = "OAUTH2",
//		type = SecuritySchemeType.OAUTH2,
//		flows = @OAuthFlows(
//				authorizationCode = @OAuthFlow(
//						authorizationUrl = "/oauth2/authorization/business-client",
//						tokenUrl = "http://127.0.0.1:8080/oauth2/token", // required for authorization code flow
//						scopes = {
//								@OAuthScope(
//										name = "order"
//								)
//						}
//				)
//		)
//)
public class SpringDocsConfig {
}
