package org.clau.pizzeriabusinessresourceserver.security;

import org.clau.apiutils.constant.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			AuthenticationHandler authenticationHandler,
			AccessDeniedHandler accessDeniedHandler,
			CookieBearerTokenResolver cookieBearerTokenResolver) throws Exception {

		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(AbstractHttpConfigurer::disable);

		http.securityMatcher(Route.API + Route.V1 + Route.ORDER_BASE + Route.ALL)
				.authorizeHttpRequests(authorize ->
						authorize.requestMatchers(Route.API + Route.V1 + Route.ORDER_BASE + Route.ALL).hasAuthority("SCOPE_order")
				);

		http.oauth2ResourceServer(oauth2ResourceServer -> {
			oauth2ResourceServer.jwt(Customizer.withDefaults());
			oauth2ResourceServer.bearerTokenResolver(cookieBearerTokenResolver); // load the brear token from a cookie
			oauth2ResourceServer.accessDeniedHandler(accessDeniedHandler); // handle access denied
			oauth2ResourceServer.authenticationEntryPoint(authenticationHandler); // handle auth failure
		});

		return http.build();
	}

	@Bean
	JwtDecoder jwtDecoder(JWTKeys keys) {
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
		decoder.setJwtValidator(JwtValidators.createDefaultWithIssuer("http://192.168.1.128:9000"));
		return decoder;
	}
}
