package org.clau.pizzeriabusinessclient.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "Pizzeria Business API", version = "v1"))
@SecurityScheme(
		name = "OAUTH2",
		type = SecuritySchemeType.OAUTH2,
		flows = @OAuthFlows(
				authorizationCode = @OAuthFlow(
						authorizationUrl = "/oauth2/authorization/business-client-oidc",
						tokenUrl = "http://127.0.0.1:8080/oauth2/token", // required for authorization code flow
						scopes = {
								@OAuthScope(
										name = "order"
								)
						}
				)
		)
)
public class SpringDocsConfig {
}
