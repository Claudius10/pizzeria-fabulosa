package org.clau.pizzeriabusinessresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.pizzeriadata.dto.business.CreatedOrderDTO;
import org.clau.pizzeriadata.dto.business.NewAnonOrderDTO;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;

@Tag(name = "Anonymous Orders API")
public interface AnonOrderControllerSwagger {

   @Operation(operationId = "createAnonOrder", summary = "Create order as an anonymous user")
   @ApiResponse(
	  responseCode = ApiResponseMessages.CREATED,
	  description = "Returns created order",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = CreatedOrderDTO.class)))
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> createAnonOrder(
	  @RequestBody(
		 required = true,
		 content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = NewAnonOrderDTO.class)))
	  @Valid NewAnonOrderDTO newAnonOrder,
	  HttpServletRequest request);
}
