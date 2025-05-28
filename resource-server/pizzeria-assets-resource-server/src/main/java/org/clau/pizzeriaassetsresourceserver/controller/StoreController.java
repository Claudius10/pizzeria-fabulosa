package org.clau.pizzeriaassetsresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsresourceserver.service.StoreService;
import org.clau.pizzeriastoreassets.model.Store;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.STORE_BASE)
public class StoreController {

	private final StoreService storeService;

	@GetMapping
	public List<Store> findAll() {
		return storeService.findAll();
	}
}
