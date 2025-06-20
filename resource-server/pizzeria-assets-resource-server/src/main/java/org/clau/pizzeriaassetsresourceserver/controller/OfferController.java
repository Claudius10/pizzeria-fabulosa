package org.clau.pizzeriaassetsresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsresourceserver.controller.swagger.OfferControllerSwagger;
import org.clau.pizzeriaassetsresourceserver.service.OfferService;
import org.clau.pizzeriastoreassets.dto.OfferListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.RESOURCE + Route.OFFER_BASE)
public class OfferController implements OfferControllerSwagger {

   private final OfferService offerService;

   @GetMapping
   public ResponseEntity<OfferListDTO> findAll() {
	  return ResponseEntity.ok(new OfferListDTO(offerService.findAll()));
   }
}
