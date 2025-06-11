package org.clau.pizzeriabackendclient.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.DelegatingAuthenticationEntryPoint;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.CompositeLogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfLogoutHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.LinkedHashMap;

import static org.springframework.security.config.Customizer.withDefaults;

// https://github.com/spring-projects/spring-authorization-server/tree/main/samples

@Configuration(proxyBeanMethods = false)
@EnableWebSecurity
public class SecurityConfig {

	@Value("${angular-app.base-uri}")
	private String angularBaseUri;

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http, ClientRegistrationRepository clientRegistrationRepository) throws Exception {
		CookieCsrfTokenRepository cookieCsrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
		CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler = new CsrfTokenRequestAttributeHandler();
		/*
		IMPORTANT:
		Set the csrfRequestAttributeName to null, to opt-out of deferred tokens, resulting in the CsrfToken to be loaded on every request.
		If it does not exist, the CookieCsrfTokenRepository will automatically generate a new one and add the Cookie to the response.
		See the reference: https://docs.spring.io/spring-security/reference/servlet/exploits/csrf.html#deferred-csrf-token
		 */
		csrfTokenRequestAttributeHandler.setCsrfRequestAttributeName(null);

		http
				.authorizeHttpRequests(authorize ->
						authorize
								.requestMatchers("/api/v1/docs.yaml").permitAll()
								.requestMatchers("/api/v1/docs/**").permitAll()
								.requestMatchers("/api/v1/resource/**").permitAll()
								.anyRequest().authenticated()
				)
				.csrf(csrf ->
						csrf
								.csrfTokenRepository(cookieCsrfTokenRepository)
								.csrfTokenRequestHandler(csrfTokenRequestAttributeHandler)
				)
				.oauth2Client(withDefaults())
				.oauth2Login(oauth2Login ->
						oauth2Login
								.successHandler(new SimpleUrlAuthenticationSuccessHandler(angularBaseUri)))
				.logout(logout ->
						logout
								.addLogoutHandler(logoutHandler(cookieCsrfTokenRepository))
								.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository))
				)
				.exceptionHandling(exceptionHandling ->
						exceptionHandling
								.authenticationEntryPoint(authenticationEntryPoint())
				);

		return http.build();
	}

	private LogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
		OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
				new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
		oidcLogoutSuccessHandler.setPostLogoutRedirectUri(angularBaseUri);

		return oidcLogoutSuccessHandler;
	}

	private AuthenticationEntryPoint authenticationEntryPoint() {
		AuthenticationEntryPoint authenticationEntryPoint =
				new LoginUrlAuthenticationEntryPoint("/oauth2/authorization/pizzeria-client");

		MediaTypeRequestMatcher textHtmlMatcher =
				new MediaTypeRequestMatcher(MediaType.TEXT_HTML);
		textHtmlMatcher.setUseEquals(true);

		LinkedHashMap<RequestMatcher, AuthenticationEntryPoint> entryPoints = new LinkedHashMap<>();
		entryPoints.put(textHtmlMatcher, authenticationEntryPoint);

		DelegatingAuthenticationEntryPoint delegatingAuthenticationEntryPoint = new DelegatingAuthenticationEntryPoint(entryPoints);
		delegatingAuthenticationEntryPoint.setDefaultEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
		return delegatingAuthenticationEntryPoint;
	}

	private LogoutHandler logoutHandler(CsrfTokenRepository csrfTokenRepository) {
		return new CompositeLogoutHandler(
				new SecurityContextLogoutHandler(),
				new CsrfLogoutHandler(csrfTokenRepository)
		);
	}
}
