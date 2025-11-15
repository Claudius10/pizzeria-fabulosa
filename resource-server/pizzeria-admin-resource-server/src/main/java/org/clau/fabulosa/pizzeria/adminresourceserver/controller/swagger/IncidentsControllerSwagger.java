package org.clau.fabulosa.pizzeria.adminresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.fabulosa.data.dto.admin.IncidenceListDTO;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Incidents API")
public interface IncidentsControllerSwagger {

   @Operation(operationId = "findAllByOriginBetweenDates", summary = "Returns all incidents by origin")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns incidents list",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = IncidenceListDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<IncidenceListDTO> findAllByOriginBetweenDates(
	  @Parameter(required = true, description = "Origin of incidents") @RequestParam String origin,
	  @Parameter(description = "Start date") @RequestParam String startDate,
	  @Parameter(description = "End date") @RequestParam String endDate
   );
}
