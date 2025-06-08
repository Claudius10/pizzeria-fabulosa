package org.clau.pizzeriauserresourceserver.security;

import org.clau.apiutils.constant.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			AuthenticationHandler authenticationHandler,
			AccessDeniedHandler accessDeniedHandler
	) throws Exception {

		http.securityMatcher(Route.API + Route.V1 + Route.USER_BASE + Route.ALL)
				.authorizeHttpRequests(authorize ->
						authorize.requestMatchers(Route.API + Route.V1 + Route.USER_BASE + Route.ALL).hasAuthority("SCOPE_user")
				);

		http.oauth2ResourceServer(oauth2ResourceServer -> {
			oauth2ResourceServer.jwt(Customizer.withDefaults());
			oauth2ResourceServer.accessDeniedHandler(accessDeniedHandler); // handle access denied
			oauth2ResourceServer.authenticationEntryPoint(authenticationHandler); // handle auth failure
		});

		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(6);
	}
}
