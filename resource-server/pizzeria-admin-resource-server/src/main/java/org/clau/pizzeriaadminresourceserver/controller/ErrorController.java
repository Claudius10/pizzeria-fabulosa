package org.clau.pizzeriaadminresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.service.CustomErrorService;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.admin.ErrorListDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ADMIN_BASE + Route.ERROR_BASE)
public class ErrorController {

   private final CustomErrorService errorService;

   @GetMapping
   public ResponseEntity<ErrorListDTO> findAllByOrigin(
	  @RequestParam(name = "origin") String origin,
	  @RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
	  @RequestParam(name = Route.PAGE_SIZE) Integer pageSize
   ) {

	  Page<APIError> allByOrigin = errorService.findAllByOrigin(origin, pageNumber, pageSize);

	  ErrorListDTO errorListDTO = new ErrorListDTO(
		 allByOrigin.getContent(),
		 allByOrigin.getNumber(),
		 allByOrigin.getSize(),
		 allByOrigin.getTotalElements(),
		 allByOrigin.isLast()
	  );

	  return ResponseEntity.ok(errorListDTO);
   }
}
