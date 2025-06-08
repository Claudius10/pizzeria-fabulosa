package org.clau.pizzeriauserclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.RegisterDTO;
import org.clau.pizzeriauserclient.service.AnonUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AnonUserServiceImpl implements AnonUserService {

	private final String path = Route.API + Route.V1 + Route.ANON_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> createUser(RegisterDTO registerDTO) {

		Mono<Object> mono = this.webClient
				.post()
				.uri(path + Route.ANON_REGISTER)
				.bodyValue(registerDTO)
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
