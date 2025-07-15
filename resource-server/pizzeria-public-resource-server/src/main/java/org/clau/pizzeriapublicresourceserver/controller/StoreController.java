package org.clau.pizzeriapublicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriapublicresourceserver.controller.swagger.StoreControllerSwagger;
import org.clau.pizzeriapublicresourceserver.service.StoreService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.assets.StoreListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.RESOURCE + Route.STORE_BASE)
public class StoreController implements StoreControllerSwagger {

   private final StoreService storeService;

   @GetMapping
   public ResponseEntity<StoreListDTO> findAll() {
	  return ResponseEntity.ok(new StoreListDTO(storeService.findAll()));
   }
}
