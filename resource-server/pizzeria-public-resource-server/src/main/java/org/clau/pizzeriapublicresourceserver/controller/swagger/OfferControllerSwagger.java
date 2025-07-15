package org.clau.pizzeriapublicresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.dto.assets.OfferListDTO;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "Offer API")
public interface OfferControllerSwagger {

   @Operation(operationId = "findAll", summary = "Returns all offers")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns offer list",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = OfferListDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OfferListDTO> findAll();
}
