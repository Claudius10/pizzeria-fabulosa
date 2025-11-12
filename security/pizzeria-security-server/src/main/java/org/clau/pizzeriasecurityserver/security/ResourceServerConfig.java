package org.clau.pizzeriasecurityserver.security;

import org.clau.pizzeriasecurityserver.security.handler.AccessDeniedHandler;
import org.clau.pizzeriasecurityserver.security.handler.AuthenticationHandler;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.enums.RoleEnum;
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
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.cors.CorsConfigurationSource;

// https://github.com/spring-projects/spring-authorization-server/tree/main/samples

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfig {

   @Bean
   SecurityFilterChain defaultSecurityFilterChain(
	  HttpSecurity http,
	  AuthenticationHandler authenticationHandler,
	  AccessDeniedHandler accessDeniedHandler,
	  CorsConfigurationSource corsConfigurationSource
   ) throws Exception {

	  http
		 .cors(cors ->
			cors.configurationSource(corsConfigurationSource))
		 .authorizeHttpRequests(authorize ->
			authorize
			   .requestMatchers(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.ALL).hasAnyRole(RoleEnum.USER.value(), RoleEnum.ADMIN.value())
			   .requestMatchers("/assets/**", "/login").permitAll()
			   .anyRequest().authenticated())
		 .formLogin(login ->
			login.loginPage("/login"));

	  http.oauth2ResourceServer(oauth2ResourceServer -> {
		 oauth2ResourceServer.jwt(jwtConfigurer ->
			jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()));
		 oauth2ResourceServer.accessDeniedHandler(accessDeniedHandler); // handle access denied
		 oauth2ResourceServer.authenticationEntryPoint(authenticationHandler); // handle auth failure
	  });

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

   private JwtAuthenticationConverter jwtAuthenticationConverter() {
	  JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
	  jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("roles");
	  jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");
	  JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
	  jwtConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
	  return jwtConverter;
   }
}
