package org.clau.pizzeriaadminresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.controller.swagger.IncidentsControllerSwagger;
import org.clau.pizzeriaadminresourceserver.service.AdminErrorService;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.admin.IncidenceListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ADMIN_BASE + Route.INCIDENTS_BASE)
public class IncidentsController implements IncidentsControllerSwagger {

   private final AdminErrorService errorService;

   @GetMapping
   public ResponseEntity<IncidenceListDTO> findAllByOriginBetweenDates(
	  @RequestParam String origin,
	  @RequestParam String startDate,
	  @RequestParam String endDate
   ) {

	  List<APIError> incidents = errorService.findAllByOriginBetweenDates(origin, startDate, endDate);

	  return ResponseEntity.ok(new IncidenceListDTO(incidents));
   }
}
