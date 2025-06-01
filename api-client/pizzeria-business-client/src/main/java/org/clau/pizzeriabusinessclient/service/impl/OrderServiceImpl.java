package org.clau.pizzeriabusinessclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.dto.OrderDTO;
import org.clau.pizzeriabusinessassets.dto.OrderSummaryListDTO;
import org.clau.pizzeriabusinessclient.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> create(Long userId, NewUserOrderDTO newUserOrder, OAuth2AuthorizedClient authorizedClient) {

		Mono<Object> mono = this.webClient
				.post()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParam(Route.USER_ID_PARAM, userId)
						.build())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.bodyValue(newUserOrder)
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(CreatedOrderDTO.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}

	@Override
	public Mono<Object> findById(Long orderId, OAuth2AuthorizedClient authorizedClient) {

		Mono<Object> mono = this.webClient
				.get()
				.uri(path + Route.ORDER_ID, orderId)
				.accept(MediaType.APPLICATION_JSON)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(OrderDTO.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}

	@Override
	public Mono<Object> deleteById(Long orderId, OAuth2AuthorizedClient authorizedClient) {

		Mono<Object> mono = this.webClient
				.delete()
				.uri(path + Route.ORDER_ID, orderId)
				.accept(MediaType.APPLICATION_JSON)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(Long.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}

	@Override
	public Mono<Object> findSummary(Long userId, int size, int page, OAuth2AuthorizedClient authorizedClient) {

		Mono<Object> mono = this.webClient
				.get()
				.uri(uriBuilder -> uriBuilder
						.path(path + Route.ORDER_SUMMARY)
						.queryParam(Route.PAGE_NUMBER, page)
						.queryParam(Route.PAGE_SIZE, size)
						.queryParam(Route.USER_ID_PARAM, userId)
						.build())
				.accept(MediaType.APPLICATION_JSON)
				.attributes(oauth2AuthorizedClient(authorizedClient))
				.exchangeToMono(response -> {
					if (response.statusCode().is2xxSuccessful()) {
						return response.bodyToMono(OrderSummaryListDTO.class);
					} else {
						return response.bodyToMono(ResponseDTO.class);
					}
				});

		return mono;
	}
}
