package org.clau.pizzeriaassetsclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.service.OfferService;
import org.clau.pizzeriastoreassets.dto.OfferListDTO;
import org.clau.pizzeriastoreassets.model.Offer;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OfferServiceImpl implements OfferService {

	private final String path = Route.API + Route.V1 + Route.OFFER_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> findAll() {
		Mono<Object> mono = this.webClient.get()
				.uri(path)
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToFlux(Offer.class).collectList().map(OfferListDTO::new);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}
}
