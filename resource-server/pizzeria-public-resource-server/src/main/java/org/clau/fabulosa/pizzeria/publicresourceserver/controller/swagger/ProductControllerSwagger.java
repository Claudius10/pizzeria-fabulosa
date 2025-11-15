package org.clau.fabulosa.pizzeria.publicresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.fabulosa.data.dto.assets.ProductListDTO;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Product API")
public interface ProductControllerSwagger {

   @Operation(operationId = "findAllByType", summary = "Returns all products by type with pagination")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns product list",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ProductListDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<ProductListDTO> findAllByType(
	  @Parameter(required = true, description = "Type of the product") @RequestParam String type,
	  @Parameter(required = true, description = "Page number starting at 0") @RequestParam(name = ApiRoutes.PARAM_PAGE_NUMBER) Integer pageNumber,
	  @Parameter(required = true, description = "Page size") @RequestParam(name = ApiRoutes.PARAM_PAGE_SIZE) Integer pageSize);
}
