package org.clau.pizzeriaassetsclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.baseUrl("http://192.168.1.128:8081")
				.build();
	}
}
