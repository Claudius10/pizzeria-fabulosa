package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.controller.swagger.OfferControllerSwagger;
import org.clau.pizzeriaassetsclient.service.OfferService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.V1 + Route.OFFER_BASE)
public class OfferController implements OfferControllerSwagger {

	private final OfferService offerService;

	@GetMapping
	public Mono<ResponseEntity<Object>> findAll() {
		Mono<ResponseEntity<Object>> result = offerService.findAll().map(response -> {
			if (response instanceof ResponseDTO) {
				return ResponseEntity.internalServerError().body(response);
			} else {
				return ResponseEntity.ok(response);
			}
		});


		return result;
	}
}
