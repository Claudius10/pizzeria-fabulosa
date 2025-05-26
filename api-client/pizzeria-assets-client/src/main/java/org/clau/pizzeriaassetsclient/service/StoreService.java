package org.clau.pizzeriaassetsclient.service;

import reactor.core.publisher.Mono;

public interface StoreService {

	Mono<Object> findAll();
}
