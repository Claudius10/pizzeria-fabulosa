package org.clau.pizzeriabusinessclient.service.impl;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.Security;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.dto.OrderDTO;
import org.clau.pizzeriabusinessassets.dto.OrderSummaryListDTO;
import org.clau.pizzeriabusinessclient.service.OrderService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

	private final String path = Route.API + Route.V1 + Route.ORDER_BASE;

	private final WebClient webClient;

	@Override
	public Mono<Object> create(Long userId, NewUserOrderDTO newUserOrder, String accessToken) {

		Mono<Object> mono = this.webClient.post()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParam(Route.USER_ID_PARAM, userId)
						.build()
				)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.cookie(Security.ACCESS_TOKEN, accessToken)
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
	public Mono<Object> findById(Long orderId, String accessToken) {

		Mono<Object> mono = this.webClient.get()
				.uri(path + Route.ORDER_ID, orderId)
				.accept(MediaType.APPLICATION_JSON)
				.cookie(Security.ACCESS_TOKEN, accessToken)
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
	public Mono<Object> deleteById(Long orderId, String accessToken) {

		Mono<Object> mono = this.webClient.delete()
				.uri(path + Route.ORDER_ID, orderId)
				.accept(MediaType.APPLICATION_JSON)
				.cookie(Security.ACCESS_TOKEN, accessToken)
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
	public Mono<Object> findSummary(Long userId, int size, int page, String accessToken) {

		Mono<Object> mono = this.webClient.get()
				.uri(uriBuilder -> uriBuilder
						.path(path)
						.queryParam(Route.PAGE_NUMBER, page)
						.queryParam(Route.PAGE_SIZE, size)
						.queryParam(Route.USER_ID_PARAM, userId)
						.build())
				.accept(MediaType.APPLICATION_JSON)
				.cookie(Security.ACCESS_TOKEN, accessToken)
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
