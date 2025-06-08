package org.clau.pizzeriauserclient.service;

import org.clau.pizzeriauserassets.dto.RegisterDTO;
import reactor.core.publisher.Mono;

public interface AnonUserService {

	Mono<Object> createUser(RegisterDTO registerDTO);
}
