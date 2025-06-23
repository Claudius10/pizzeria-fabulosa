package org.clau.pizzeriasecurityserver.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.property.MyURI;
import org.clau.pizzeriasecurityserver.security.jose.Jwks;
import org.clau.pizzeriasecurityserver.service.impl.OidcUserInfoServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.InMemoryOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationContext;
import org.springframework.security.oauth2.server.authorization.oidc.authentication.OidcUserInfoAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Duration;
import java.util.UUID;
import java.util.function.Function;

import static org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer.authorizationServer;

// https://github.com/spring-projects/spring-authorization-server/tree/main/samples

@RequiredArgsConstructor
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfig {

   private final MyURI uri;

   @Bean
   @Order(Ordered.HIGHEST_PRECEDENCE)
   SecurityFilterChain authorizationServerSecurityFilterChain(
	  HttpSecurity http,
	  OidcUserInfoServiceImpl userInfoService,
	  CorsConfigurationSource corsConfigurationSource) throws Exception {

	  Function<OidcUserInfoAuthenticationContext, OidcUserInfo> userInfoMapper = (context) -> {
		 OidcUserInfoAuthenticationToken authentication = context.getAuthentication();
		 JwtAuthenticationToken principal = (JwtAuthenticationToken) authentication.getPrincipal();
		 OidcUserInfo oidcUserInfo = userInfoService.loadUser(principal.getName());
		 return new OidcUserInfo(oidcUserInfo.getClaims());
	  };

	  OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = authorizationServer();

	  http
		 .cors(cors ->
			cors.configurationSource(corsConfigurationSource))
		 .securityMatcher(authorizationServerConfigurer.getEndpointsMatcher())
		 .with(authorizationServerConfigurer, (authorizationServer) ->
			authorizationServer.oidc(oidc ->
			   oidc.userInfoEndpoint(userInfo ->
				  userInfo.userInfoMapper(userInfoMapper)))
		 )
		 .authorizeHttpRequests((authorize) ->
			authorize.anyRequest().authenticated())
		 // Redirect to the /login page when not authenticated from the authorization endpoint
		 .exceptionHandling((exceptions) ->
			exceptions.defaultAuthenticationEntryPointFor(
			   new LoginUrlAuthenticationEntryPoint("/login"),
			   new MediaTypeRequestMatcher(MediaType.TEXT_HTML)
			)
		 );

	  return http.build();
   }

   @Bean
   RegisteredClientRepository registeredClientRepository(PasswordEncoder bCrypt) {

	  String secret = bCrypt.encode("pizzeria");

	  RegisteredClient pizzeriaClient = RegisteredClient.withId(UUID.randomUUID().toString())
		 .clientId("pizzeria-client")
		 .clientSecret(secret)
		 .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
		 .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
		 .redirectUri(uri.getPizzeriaApiBase() + "/login/oauth2/code/pizzeria-client")
		 .postLogoutRedirectUri(uri.getAngularBase())
		 .scope(OidcScopes.OPENID)
		 .scope("user")
		 .scope("order")
		 .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
		 .tokenSettings(TokenSettings.builder()
			.authorizationCodeTimeToLive(Duration.ofMinutes(5))
			.accessTokenTimeToLive(Duration.ofDays(1))
			.accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
			.idTokenSignatureAlgorithm(SignatureAlgorithm.RS256)
			.x509CertificateBoundAccessTokens(false)
			.build())
		 .build();

	  return new InMemoryRegisteredClientRepository(pizzeriaClient);
   }

   @Bean
   OAuth2AuthorizationService authorizationService() {
	  return new InMemoryOAuth2AuthorizationService();
   }

   @Bean
   JWKSource<SecurityContext> jwkSource() {
	  RSAKey rsaKey = Jwks.generateRsa();
	  JWKSet jwkSet = new JWKSet(rsaKey);
	  return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
   }

   @Bean
   JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
	  return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
   }

   @Bean
   AuthorizationServerSettings authorizationServerSettings() {
	  return AuthorizationServerSettings.builder().build();
   }
}
