package org.clau.pizzeriabusinessclient.service;

import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import reactor.core.publisher.Mono;

public interface OrderService {

	Mono<Object> create(Long userId, NewUserOrderDTO newUserOrder, String accessToken);

	Mono<Object> findById(Long orderId, String accessToken);

	Mono<Object> deleteById(Long orderId, String accessToken);

	Mono<Object> findSummary(Long userId, int size, int page, String accessToken);
}
