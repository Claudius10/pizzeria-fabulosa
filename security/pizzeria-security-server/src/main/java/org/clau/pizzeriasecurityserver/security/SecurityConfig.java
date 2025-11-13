package org.clau.pizzeriasecurityserver.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfigurationSource;

// https://github.com/spring-projects/spring-authorization-server/tree/main/samples

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

   @Bean
   SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, CorsConfigurationSource corsConfigurationSource) throws Exception {

	  http
		 .cors(cors ->
			cors.configurationSource(corsConfigurationSource))
		 .authorizeHttpRequests(authorize ->
			authorize
			   .requestMatchers("/assets/**", "/login", "/api/v1/register/**").permitAll()
			   .anyRequest().authenticated())
		 .formLogin(login ->
			login.loginPage("/login"));

	  return http.build();
   }

   @Bean
   PasswordEncoder passwordEncoder() {
	  return new BCryptPasswordEncoder(6);
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
}
