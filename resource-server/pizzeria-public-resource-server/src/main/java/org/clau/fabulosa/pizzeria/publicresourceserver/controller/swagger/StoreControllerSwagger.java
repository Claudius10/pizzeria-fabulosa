package org.clau.fabulosa.pizzeria.publicresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.fabulosa.data.dto.assets.StoreListDTO;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;

@Tag(name = "Store API")
public interface StoreControllerSwagger {

   @Operation(operationId = "findAll", summary = "Returns all stores")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns store list",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = StoreListDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<StoreListDTO> findAll();
}
