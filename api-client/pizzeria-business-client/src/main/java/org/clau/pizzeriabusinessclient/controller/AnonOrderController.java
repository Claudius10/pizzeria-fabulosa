package org.clau.pizzeriabusinessclient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessclient.controller.swagger.AnonOrderControllerSwagger;
import org.clau.pizzeriabusinessclient.service.AnonOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ANON_BASE + Route.ORDER_BASE)
public class AnonOrderController implements AnonOrderControllerSwagger {

	private final AnonOrderService anonOrderService;

	@PostMapping
	public Mono<ResponseEntity<Object>> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrderDTO) {
		Mono<ResponseEntity<Object>> result = anonOrderService.createAnonOrder(newAnonOrderDTO).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}
}
