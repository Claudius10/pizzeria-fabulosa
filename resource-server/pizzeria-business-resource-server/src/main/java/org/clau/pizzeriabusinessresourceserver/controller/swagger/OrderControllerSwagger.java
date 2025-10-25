package org.clau.pizzeriabusinessresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.business.CreatedOrderDTO;
import org.clau.pizzeriautils.dto.business.NewUserOrderDTO;
import org.clau.pizzeriautils.dto.business.OrderDTO;
import org.clau.pizzeriautils.dto.business.OrderSummaryListDTO;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User Orders API")
public interface OrderControllerSwagger {

   @Operation(operationId = "create", summary = "Create user order")
   @ApiResponse(
	  responseCode = Response.CREATED,
	  description = "Returns created order",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = CreatedOrderDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> create(
	  @Valid NewUserOrderDTO order,
	  @Parameter(required = true, description = "Id of the user for which to create the order") @RequestParam(name = Route.USER_ID_PARAM) Long userId,
	  HttpServletRequest request);


   @Operation(operationId = "findById", summary = "Find user order by id")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns user order",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = OrderDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.NO_CONTENT,
	  description = "User order not found",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderDTO> findById(@Parameter(required = true, description = "Id of the order to find") @PathVariable Long orderId);


   @Operation(operationId = "cancelById", summary = "Cancel user order by id")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns the canceled order's id",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = Long.class))
   )
   @ApiResponse(
	  responseCode = Response.NO_CONTENT,
	  description = "User order not found",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Validation failed or invalid request or order cancel time-limit passed",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> cancelById(@Parameter(required = true, description = "Id of the order to cancel") @PathVariable Long orderId,
								HttpServletRequest request);


   @Operation(operationId = "findSummary", summary = "Returns user orders summary with pagination")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns user orders summary",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = OrderSummaryListDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.NO_CONTENT,
	  description = "User orders summary is empty",
	  content = @Content()
   )
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Validation failed or invalid request",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.UNAUTHORIZED,
	  description = "User authentification failed",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderSummaryListDTO> findSummary(
	  @Parameter(required = true, description = "Page number starting at 0") @RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
	  @Parameter(required = true, description = "Page size") @RequestParam(name = Route.PAGE_SIZE) Integer pageSize,
	  @Parameter(required = true, description = "Id of the user for which to find the summary") @RequestParam(name = Route.USER_ID_PARAM) Long userId);
}
