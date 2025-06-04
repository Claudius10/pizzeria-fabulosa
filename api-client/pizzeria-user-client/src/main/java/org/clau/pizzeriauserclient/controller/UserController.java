package org.clau.pizzeriauserclient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.pizzeriauserclient.util.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final WebClient webClient;

	@GetMapping("/logged-in")
	public void userIn() {
		log.info("{} logged IN", Constant.APP_NAME);
	}

	@GetMapping("/logged-out")
	public void userOut() {
		log.info("{} logged OUT", Constant.APP_NAME);
	}

	@GetMapping("/user")
	public ResponseEntity<?> userInfo(@RegisteredOAuth2AuthorizedClient("user-client") OAuth2AuthorizedClient authorizedClient) {
		Object userInfo = this.webClient
				.get()
				.uri("http://localhost:9000/userinfo")
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(Object.class);
					} else {
						return response.createError();
					}
				}).block();

		return ResponseEntity.ok(userInfo);
	}
}
