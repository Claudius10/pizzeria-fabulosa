package org.clau.pizzeriabusinessclient.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.CreatedOrderDTO;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Mono;

@Tag(name = "Anonymous User API")
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
	Mono<ResponseEntity<Object>> createAnonOrder(
			@RequestBody(
					required = true,
					content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = NewAnonOrderDTO.class)))
			@Valid NewAnonOrderDTO newAnonOrder);
}
