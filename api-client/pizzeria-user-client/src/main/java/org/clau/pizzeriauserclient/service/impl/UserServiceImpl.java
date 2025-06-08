package org.clau.pizzeriauserclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserclient.service.UserService;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

	private final ParameterizedTypeReference<Map<String, Object>> userInfoClaims = new ParameterizedTypeReference<>() {
	};

	private final String path = Route.API + Route.V1 + Route.USER_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Map<String, Object>> getInfo(OAuth2AuthorizedClient authorizedClient) {

		Mono<Map<String, Object>> mono = this.webClient
				.get()
				.uri("http://localhost:9000/userinfo")
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.retrieve()
				.bodyToMono(userInfoClaims);

		return mono;
	}

	@Override
	public Mono<Object> deleteById(Long userId, OAuth2AuthorizedClient authorizedClient) {

		Mono<Object> mono = this.webClient
				.delete()
				.uri(path + Route.USER_ID, userId)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(Void.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}
}
