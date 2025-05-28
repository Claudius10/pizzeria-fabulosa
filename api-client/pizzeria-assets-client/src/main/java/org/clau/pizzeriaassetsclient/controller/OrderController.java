package org.clau.pizzeriaassetsclient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriaassetsclient.service.OrderService;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

	private final OrderService orderService;

	@PostMapping
	public Mono<ResponseEntity<Object>> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrderDTO) {
		Mono<ResponseEntity<Object>> result = orderService.createAnonOrder(newAnonOrderDTO).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}
}
