package org.clau.fabulosa.pizzeria.publicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.dto.assets.StoreListDTO;
import org.clau.fabulosa.pizzeria.publicresourceserver.controller.swagger.StoreControllerSwagger;
import org.clau.fabulosa.pizzeria.publicresourceserver.service.StoreService;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.RESOURCE + ApiRoutes.STORE)
public class StoreController implements StoreControllerSwagger {

   private final StoreService storeService;

   @GetMapping
   public ResponseEntity<StoreListDTO> findAll() {
	  return ResponseEntity.ok(new StoreListDTO(storeService.findAll()));
   }
}
