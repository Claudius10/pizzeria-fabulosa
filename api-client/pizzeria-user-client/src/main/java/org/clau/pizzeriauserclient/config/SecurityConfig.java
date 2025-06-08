package org.clau.pizzeriauserclient.config;

import org.clau.apiutils.constant.Route;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(
			HttpSecurity http,
			ClientRegistrationRepository clientRegistrationRepository) throws Exception {

		http
				.authorizeHttpRequests(authorize ->
						authorize
								.requestMatchers("/logged-out").permitAll()
								.requestMatchers(Route.API + Route.V1 + Route.ANON_BASE + Route.ALL).permitAll()
								.anyRequest().authenticated())
				.oauth2Client(withDefaults())
				.oauth2Login(withDefaults())
				.logout(logout ->
						logout.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)));

		return http.build();
	}

	/**
	 * Performs OIDC logout.
	 */
	private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {

		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

		// Set the location that the End-User's User Agent will be redirected to
		// after the logout has been performed at the Provider
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}/logged-out");

		return oidcLogoutSuccessHandler;
	}
}
