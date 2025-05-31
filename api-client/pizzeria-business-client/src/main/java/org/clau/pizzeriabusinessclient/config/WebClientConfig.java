package org.clau.pizzeriabusinessclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration(proxyBeanMethods = false)
public class WebClientConfig {

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.baseUrl("http://192.168.1.128:8083")
				.build();
	}
}
