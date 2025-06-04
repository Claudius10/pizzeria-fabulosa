package org.clau.pizzeriabusinessclient.config;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ssl.SslBundle;
import org.springframework.boot.ssl.SslBundles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.tcp.SslProvider;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;

@Profile("!test")
@Configuration(proxyBeanMethods = false)
public class WebClientConfig {

	@Bean
	WebClient webClient(
			@Value("${business-resource.base-uri}") String baseUri,
			OAuth2AuthorizedClientManager authorizedClientManager,
			SslBundles sslBundles) throws Exception {

		ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
				new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

		SslBundle bundle = sslBundles.getBundle("business-client-ssl");
		ClientHttpConnector clientConnector = createClientConnector(bundle);

		return WebClient.builder()
				.baseUrl(baseUri)
				.clientConnector(clientConnector)
				.apply(oauth2.oauth2Configuration())
				.build();
	}

	@Bean
	OAuth2AuthorizedClientManager authorizedClientManager(
			ClientRegistrationRepository clientRegistrationRepository,
			OAuth2AuthorizedClientRepository authorizedClientRepository) {

		OAuth2AuthorizedClientProvider authorizedClientProvider =
				OAuth2AuthorizedClientProviderBuilder.builder()
						.authorizationCode()
						.refreshToken()
						.clientCredentials()
						.build();

		DefaultOAuth2AuthorizedClientManager authorizedClientManager =
				new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);

		authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

		return authorizedClientManager;
	}

	private static ClientHttpConnector createClientConnector(SslBundle sslBundle) throws Exception {
		KeyManagerFactory keyManagerFactory = sslBundle.getManagers().getKeyManagerFactory();
		TrustManagerFactory trustManagerFactory = sslBundle.getManagers().getTrustManagerFactory();

		SslContext sslContext = SslContextBuilder
				.forClient()
				.keyManager(keyManagerFactory)
				.trustManager(trustManagerFactory)
				.build();

		SslProvider sslProvider = SslProvider.builder().sslContext(sslContext).build();
		HttpClient httpClient = HttpClient.create().secure(sslProvider);
		return new ReactorClientHttpConnector(httpClient);
	}
}
