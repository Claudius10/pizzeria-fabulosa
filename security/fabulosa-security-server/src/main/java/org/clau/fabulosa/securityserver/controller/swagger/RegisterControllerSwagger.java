package org.clau.fabulosa.securityserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.securityserver.data.dto.RegisterDTO;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;

@Tag(name = "Register API")
public interface RegisterControllerSwagger {

   @Operation(operationId = "registerAnonUser", summary = "Register user")
   @ApiResponse(
	  responseCode = ApiResponseMessages.CREATED,
	  description = "Registration successful",
	  content = @Content())
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
   ResponseEntity<?> registerAnonUser(
	  @RequestBody(
		 required = true,
		 content = @Content(
			mediaType = ApiResponseMessages.JSON,
			schema = @Schema(implementation = RegisterDTO.class)
		 ))
	  @Valid RegisterDTO registerDTO, HttpServletRequest request);
}
