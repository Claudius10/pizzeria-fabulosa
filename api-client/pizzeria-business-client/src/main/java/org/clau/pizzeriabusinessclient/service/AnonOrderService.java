package org.clau.pizzeriabusinessclient.service;

import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import reactor.core.publisher.Mono;

public interface AnonOrderService {

	Mono<Object> createAnonOrder(NewAnonOrderDTO newAnonOrderDTO);
}
