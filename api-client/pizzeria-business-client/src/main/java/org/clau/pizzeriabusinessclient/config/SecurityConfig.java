package org.clau.pizzeriabusinessclient.config;

import org.clau.apiutils.constant.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests(authorize -> {
							authorize.requestMatchers(Route.API + Route.V1 + Route.ANON_BASE + Route.ORDER_BASE + Route.ALL).permitAll();
							authorize.requestMatchers(Route.API + Route.V1 + Route.DOCS + Route.ALL).permitAll();
							authorize.anyRequest().authenticated();
						}
				)
				.oauth2Client(withDefaults())
				.oauth2Login(withDefaults());

		return http.build();
	}
}
