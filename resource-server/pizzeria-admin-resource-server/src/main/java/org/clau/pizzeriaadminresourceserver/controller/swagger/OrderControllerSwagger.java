package org.clau.pizzeriaadminresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Order Statistics API")
public interface OrderControllerSwagger {

   @Operation(operationId = "findCountForTimelineAndState", summary = "Returns order count for given timeline and state")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns DTO",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = OrderStatisticsByState.class))
   )
   @ApiResponse(
	  responseCode = Response.BAD_REQUEST,
	  description = "Unsupported order state",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> findCountForTimelineAndState(
	  HttpServletRequest request,
	  @Parameter(required = true, description = "hourly, daily, monthly, yearly") @RequestParam String timeline,
	  @Parameter(required = true, description = "COMPLETED, CANCELLED") @RequestParam String state
   );
}
