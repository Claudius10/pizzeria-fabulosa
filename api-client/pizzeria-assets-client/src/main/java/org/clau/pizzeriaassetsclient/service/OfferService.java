package org.clau.pizzeriaassetsclient.service;

import reactor.core.publisher.Mono;

public interface OfferService {

	Mono<Object> findAll();
}
