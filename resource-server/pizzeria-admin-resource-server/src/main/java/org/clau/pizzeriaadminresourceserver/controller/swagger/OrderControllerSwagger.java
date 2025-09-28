package org.clau.pizzeriaadminresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Tag(name = "Order Statistics API")
public interface OrderControllerSwagger {

   @Operation(operationId = "findCountForTimeline", summary = "Returns order count for given timeline")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "Returns array of integers",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = List.class))
   )
   @ApiResponse(
	  responseCode = Response.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<List<Integer>> findCountForTimeline(
	  @Parameter(required = true, description = "hourly, daily, monthly, yearly") @RequestParam String timeline
   );
}
