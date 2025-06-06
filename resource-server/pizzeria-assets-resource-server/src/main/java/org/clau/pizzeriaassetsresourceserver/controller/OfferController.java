package org.clau.pizzeriaassetsresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsresourceserver.service.OfferService;
import org.clau.pizzeriastoreassets.model.Offer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.OFFER_BASE)
public class OfferController {

	private final OfferService offerService;

	@GetMapping
	public List<Offer> findAll() {
		return offerService.findAll();
	}
}
