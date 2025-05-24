package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsclient.controller.swagger.OfferControllerSwagger;
import org.clau.pizzeriaassetsclient.dto.OfferListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.OFFER_BASE + Route.V1)
public class OfferController implements OfferControllerSwagger {


	@GetMapping
	public ResponseEntity<OfferListDTO> findAll() {
		return null;
	}
}
