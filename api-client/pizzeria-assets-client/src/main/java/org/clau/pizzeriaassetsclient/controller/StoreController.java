package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.controller.swagger.StoreControllerSwagger;
import org.clau.pizzeriaassetsclient.service.StoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.STORE_BASE)
public class StoreController implements StoreControllerSwagger {

	private final StoreService storeService;

	@GetMapping
	public Mono<ResponseEntity<Object>> findAll() {
		Mono<ResponseEntity<Object>> result = storeService.findAll().map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.ok(response);
			}
		});

		return result;
	}
}
