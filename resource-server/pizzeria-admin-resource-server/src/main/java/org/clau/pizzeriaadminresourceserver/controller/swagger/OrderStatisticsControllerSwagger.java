package org.clau.pizzeriaadminresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.pizzeriadata.dto.admin.OrderStatistics;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Order Statistics API")
public interface OrderStatisticsControllerSwagger {

   @Operation(operationId = "findCountByOrderState", summary = "Returns order count for the given timeline and all order states",
	  description = "First index is for completed orders; second index is for cancelled orders")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns DTO",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = OrderStatistics.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Unsupported order state",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderStatistics> findCountByOrderState(@Parameter(required = true, description = "hourly, daily, monthly, yearly") @RequestParam String timeline
   );

   @Operation(operationId = "findCountByUserState", summary = "Returns order count for the given timeline and all user states",
	  description = "First index is for registered users; second index is for anonymous users")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns DTO",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = OrderStatistics.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.BAD_REQUEST,
	  description = "Unsupported order state",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<OrderStatistics> findCountByUserState(@Parameter(required = true, description = "hourly, daily, monthly, yearly, all") @RequestParam String timeline
   );
}
