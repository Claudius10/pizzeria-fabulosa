package org.clau.pizzeriaassetsclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.service.ProductService;
import org.clau.pizzeriastoreassets.dto.ProductListDTO;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

	private final String path = Route.API + Route.V1 + Route.PRODUCT_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> findAllByType(String productType, int size, int page) {
		Mono<Object> mono = this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParam(Route.PRODUCT_TYPE, productType)
						.queryParam(Route.PAGE_SIZE, size)
						.queryParam(Route.PAGE_NUMBER, page)
						.build())
				.accept(MediaType.APPLICATION_JSON)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(ProductListDTO.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}
}
