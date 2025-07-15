package org.clau.pizzeriauserresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.dto.user.RegisterDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "Register API")
public interface RegisterControllerSwagger {

   @Operation(operationId = "registerAnonUser", summary = "Register user")
   @ApiResponse(
	  responseCode = Response.CREATED,
	  description = "Registration successful",
	  content = @Content())
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
   ResponseEntity<?> registerAnonUser(
	  @RequestBody(
		 required = true,
		 content = @Content(
			mediaType = Response.JSON,
			schema = @Schema(implementation = RegisterDTO.class)
		 ))
	  @Valid RegisterDTO registerDTO, HttpServletRequest request);
}
