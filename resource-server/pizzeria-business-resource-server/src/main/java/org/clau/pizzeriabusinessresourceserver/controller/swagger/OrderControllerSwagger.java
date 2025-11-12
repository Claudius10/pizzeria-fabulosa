package org.clau.pizzeriabusinessresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.pizzeriadata.dto.business.CreatedOrderDTO;
import org.clau.pizzeriadata.dto.business.NewUserOrderDTO;
import org.clau.pizzeriadata.dto.business.OrderDTO;
import org.clau.pizzeriadata.dto.business.OrderSummaryListDTO;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Orders API")
public interface OrderControllerSwagger {

   @Operation(operationId = "create", summary = "Create user order")
   @ApiResponse(
	  responseCode = ApiResponseMessages.CREATED,
	  description = "Returns created order",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = CreatedOrderDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> create(
	  @Valid NewUserOrderDTO order,
	  @Parameter(required = true, description = "Id of the user for which to create the order") @RequestParam(name = ApiRoutes.PARAM_USER_ID) Long userId,
	  HttpServletRequest request);


   @Operation(operationId = "findById", summary = "Find user order by id")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns user order",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = OrderDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.NO_CONTENT,
	  description = "User order not found",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderDTO> findById(@Parameter(required = true, description = "Id of the order to find") @PathVariable Long orderId);


   @Operation(operationId = "cancelById", summary = "Cancel user order by id")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns the canceled order's id",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = Long.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.NO_CONTENT,
	  description = "User order not found",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Validation failed or invalid request or order cancel time-limit passed",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> cancelById(@Parameter(required = true, description = "Id of the order to cancel") @PathVariable Long orderId,
								HttpServletRequest request);


   @Operation(operationId = "findSummary", summary = "Returns user orders summary with pagination")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns user orders summary",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = OrderSummaryListDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.NO_CONTENT,
	  description = "User orders summary is empty",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderSummaryListDTO> findSummary(
	  @Parameter(required = true, description = "Page number starting at 0") @RequestParam(name = ApiRoutes.PARAM_PAGE_NUMBER) Integer pageNumber,
	  @Parameter(required = true, description = "Page size") @RequestParam(name = ApiRoutes.PARAM_PAGE_SIZE) Integer pageSize,
	  @Parameter(required = true, description = "Id of the user for which to find the summary") @RequestParam(name = ApiRoutes.PARAM_USER_ID) Long userId);
}
