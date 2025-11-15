package org.clau.fabulosa.pizzeria.publicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.dto.assets.OfferListDTO;
import org.clau.fabulosa.pizzeria.publicresourceserver.controller.swagger.OfferControllerSwagger;
import org.clau.fabulosa.pizzeria.publicresourceserver.service.OfferService;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.RESOURCE + ApiRoutes.OFFER)
public class OfferController implements OfferControllerSwagger {

   private final OfferService offerService;

   @GetMapping
   public ResponseEntity<OfferListDTO> findAll() {
	  return ResponseEntity.ok(new OfferListDTO(offerService.findAll()));
   }
}
