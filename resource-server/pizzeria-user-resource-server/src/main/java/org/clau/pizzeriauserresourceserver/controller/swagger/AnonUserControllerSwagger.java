package org.clau.pizzeriauserresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.RegisterDTO;
import org.springframework.http.ResponseEntity;

@Tag(name = "Anonymous User API", description = "Anonymous user related operations")
public interface AnonUserControllerSwagger {

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
