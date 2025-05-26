package org.clau.pizzeriaassetsclient.service;

import reactor.core.publisher.Mono;

public interface ProductService {

	Mono<Object> findAllByType(String productType, int size, int page);
}
