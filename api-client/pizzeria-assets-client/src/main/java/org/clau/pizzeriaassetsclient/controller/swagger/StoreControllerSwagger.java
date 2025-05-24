package org.clau.pizzeriaassetsclient.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.dto.StoreListDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "Store API")
public interface StoreControllerSwagger {

	@Operation(operationId = "findAll", summary = "Returns all stores")
	@ApiResponse(
			responseCode = Response.OK,
			description = "Returns store list",
			content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = StoreListDTO.class))
	)
	@ApiResponse(
			responseCode = Response.INTERNAL_SERVER_ERROR,
			description = "Unexpected exception occurred",
			content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
	)
	ResponseEntity<StoreListDTO> findAll();
}
