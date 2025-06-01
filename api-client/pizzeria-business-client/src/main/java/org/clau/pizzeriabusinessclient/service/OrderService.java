package org.clau.pizzeriabusinessclient.service;

import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import reactor.core.publisher.Mono;

public interface OrderService {

	Mono<Object> create(Long userId, NewUserOrderDTO newUserOrder, OAuth2AuthorizedClient authorizedClient);

	Mono<Object> findById(Long orderId, OAuth2AuthorizedClient authorizedClient);

	Mono<Object> deleteById(Long orderId, OAuth2AuthorizedClient authorizedClient);

	Mono<Object> findSummary(Long userId, int size, int page, OAuth2AuthorizedClient authorizedClient);
}
