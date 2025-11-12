package org.clau.pizzeriaadminresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.clau.pizzeriadata.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "Order Statistics API")
public interface OrderControllerSwagger {

   @Operation(operationId = "findCountForTimelineAndState", summary = "Returns order count for given timeline and state")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns DTO",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = OrderStatisticsByState.class))
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
   ResponseEntity<?> findCountForTimelineAndState(
	  HttpServletRequest request,
	  @Parameter(required = true, description = "hourly, daily, monthly, yearly") @RequestParam String timeline,
	  @Parameter(required = true, description = "COMPLETED, CANCELLED") @RequestParam String state
   );
}
