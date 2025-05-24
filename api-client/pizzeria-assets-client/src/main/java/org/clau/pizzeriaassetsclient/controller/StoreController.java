package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsclient.controller.swagger.StoreControllerSwagger;
import org.clau.pizzeriaassetsclient.dto.StoreListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.STORE_BASE + Route.V1)
public class StoreController implements StoreControllerSwagger {

	@GetMapping
	public ResponseEntity<StoreListDTO> findAll() {
		return null;
	}
}
