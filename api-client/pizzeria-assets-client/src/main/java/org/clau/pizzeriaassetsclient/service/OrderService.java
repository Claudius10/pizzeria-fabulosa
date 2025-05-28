package org.clau.pizzeriaassetsclient.service;

import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import reactor.core.publisher.Mono;

public interface OrderService {

	Mono<Object> createAnonOrder(NewAnonOrderDTO newAnonOrderDTO);
}
