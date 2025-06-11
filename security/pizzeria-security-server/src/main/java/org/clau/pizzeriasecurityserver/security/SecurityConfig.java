package org.clau.pizzeriasecurityserver.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Value("${angular-app.base-uri}")
	private String angularBaseUri;

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http
				.cors(cors ->
						cors.configurationSource(corsConfigurationSource()))
				.authorizeHttpRequests(authorize ->
						authorize.anyRequest().authenticated())
				.formLogin(Customizer.withDefaults());

		return http.build();
	}

	// TODO
	@Bean
	PasswordEncoder passwordEncoder() {
		return new PasswordEncoder() {
			@Override
			public String encode(CharSequence rawPassword) {
				return "";
			}

			@Override
			public boolean matches(CharSequence rawPassword, String encodedPassword) {
				return true;
			}
		};
	}

	@Bean
	AuthenticationManager authManager(UserDetailsService userDetailsService, PasswordEncoder bCrypt) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
		authenticationProvider.setPasswordEncoder(bCrypt);
		authenticationProvider.setHideUserNotFoundExceptions(false);
		return new ProviderManager(authenticationProvider);
	}

	@Bean
	SessionRegistry sessionRegistry() {
		// If OpenID Connect 1.0 is enabled, a SessionRegistry instance is used to track authenticated sessions.
		return new SessionRegistryImpl();
	}

	@Bean
	HttpSessionEventPublisher httpSessionEventPublisher() {
		// HttpSessionEventPublisher is responsible for notifying SessionRegistryImpl of session lifecycle events,
		// for example, SessionDestroyedEvent, to provide the ability to remove the SessionInformation instance.
		return new HttpSessionEventPublisher();
	}

	private CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		config.setAllowedOrigins(Collections.singletonList(angularBaseUri));

		config.addAllowedHeader("X-XSRF-TOKEN");
		config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);

		config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
		config.setAllowCredentials(true);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
}
