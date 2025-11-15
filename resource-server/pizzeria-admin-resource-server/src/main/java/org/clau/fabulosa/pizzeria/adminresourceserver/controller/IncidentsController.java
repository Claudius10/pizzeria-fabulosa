package org.clau.fabulosa.pizzeria.adminresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.pizzeria.adminresourceserver.controller.swagger.IncidentsControllerSwagger;
import org.clau.fabulosa.pizzeria.adminresourceserver.service.AdminErrorService;
import org.clau.fabulosa.data.dto.admin.IncidenceListDTO;
import org.clau.fabulosa.data.model.common.APIError;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ADMIN + ApiRoutes.INCIDENTS)
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
