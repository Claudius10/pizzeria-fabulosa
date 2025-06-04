package org.clau.pizzeriabusinessclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessclient.service.AnonOrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class AnonOrderServiceImpl implements AnonOrderService {

	private final String path = Route.API + Route.V1 + Route.ANON_BASE + Route.ORDER_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> createAnonOrder(NewAnonOrderDTO newAnonOrderDTO) {

		Mono<Object> mono = this.webClient.post()
				.uri(path)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.bodyValue(newAnonOrderDTO)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(CreatedOrderDTO.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}
}
