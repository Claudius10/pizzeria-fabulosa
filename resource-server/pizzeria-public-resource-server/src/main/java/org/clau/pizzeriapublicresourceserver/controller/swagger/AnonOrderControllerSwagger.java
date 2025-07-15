package org.clau.pizzeriapublicresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.dto.business.CreatedOrderDTO;
import org.clau.pizzeriautils.dto.business.NewAnonOrderDTO;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "Anonymous Orders API")
public interface AnonOrderControllerSwagger {

   @Operation(operationId = "createAnonOrder", summary = "Create order as an anonymous user")
   @ApiResponse(
	  responseCode = Response.CREATED,
	  description = "Returns created order",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = CreatedOrderDTO.class)))
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> createAnonOrder(
	  @RequestBody(
		 required = true,
		 content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = NewAnonOrderDTO.class)))
	  @Valid NewAnonOrderDTO newAnonOrder,
	  HttpServletRequest request);
}
