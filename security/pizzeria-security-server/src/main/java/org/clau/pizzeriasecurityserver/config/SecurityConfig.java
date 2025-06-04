package org.clau.pizzeriasecurityserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		http
				.authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
				.formLogin(login -> login.defaultSuccessUrl("/logged-in", true));

		return http.build();
	}

	@Bean
	public UserDetailsService users() {
		UserDetails user = User.withDefaultPasswordEncoder()
				.username("user")
				.password("password")
				.roles("USER")
				.build();

		UserDetails userTwo = User.withDefaultPasswordEncoder()
				.username("userTwo")
				.password("password")
				.roles("USER")
				.build();

		return new InMemoryUserDetailsManager(user, userTwo);
	}

	@Bean
	public SessionRegistry sessionRegistry() {
		// If OpenID Connect 1.0 is enabled, a SessionRegistry instance is used to track authenticated sessions.
		return new SessionRegistryImpl();
	}

	@Bean
	public HttpSessionEventPublisher httpSessionEventPublisher() {
		// HttpSessionEventPublisher is responsible for notifying SessionRegistryImpl of session lifecycle events,
		// for example, SessionDestroyedEvent, to provide the ability to remove the SessionInformation instance.
		return new HttpSessionEventPublisher();
	}
}
