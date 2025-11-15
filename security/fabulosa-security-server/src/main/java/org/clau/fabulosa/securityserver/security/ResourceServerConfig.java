package org.clau.fabulosa.securityserver.security;

import org.clau.fabulosa.securityserver.security.handler.AccessDeniedHandler;
import org.clau.fabulosa.securityserver.security.handler.AuthenticationHandler;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.clau.fabulosa.utils.enums.RoleEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class ResourceServerConfig {

   @Bean
   SecurityFilterChain resourceServerSecurityFilterChain(HttpSecurity http, AuthenticationHandler authenticationHandler, AccessDeniedHandler accessDeniedHandler) throws Exception {

	  http
		 .securityMatcher(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.ALL)
		 .authorizeHttpRequests(authorize ->
			authorize.requestMatchers(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE + ApiRoutes.ALL).hasAnyRole(RoleEnum.USER.value(), RoleEnum.ADMIN.value()))
		 .csrf(AbstractHttpConfigurer::disable) // bearer-token API; CSRF not needed
		 .oauth2ResourceServer(oauth2ResourceServer -> {
			oauth2ResourceServer.jwt(jwtConfigurer ->
			   jwtConfigurer.jwtAuthenticationConverter(jwtAuthenticationConverter()));
			oauth2ResourceServer.accessDeniedHandler(accessDeniedHandler); // handle access denied
			oauth2ResourceServer.authenticationEntryPoint(authenticationHandler); // handle auth failure
		 });

	  return http.build();
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
