package org.clau.pizzeriaassetsclient.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriastoreassets.dto.ProductListDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Tag(name = "Product API")
public interface ProductControllerSwagger {

	@Operation(operationId = "findAllByType", summary = "Returns all products by type with pagination")
	@ApiResponse(
			responseCode = Response.OK,
			description = "Returns product list",
			content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ProductListDTO.class))
	)
	@ApiResponse(
			responseCode = Response.INTERNAL_SERVER_ERROR,
			description = "Unexpected exception occurred",
			content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
	)
	Mono<ResponseEntity<Object>> findAllByType(
			@Parameter(required = true, description = "Type of the product") @RequestParam String type,
			@Parameter(required = true, description = "Page number starting at 0") @RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@Parameter(required = true, description = "Page size") @RequestParam(name = Route.PAGE_SIZE) Integer pageSize);
}
