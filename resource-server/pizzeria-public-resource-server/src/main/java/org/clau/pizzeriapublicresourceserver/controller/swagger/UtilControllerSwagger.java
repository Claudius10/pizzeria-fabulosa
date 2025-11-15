package org.clau.pizzeriapublicresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Tag(name = "Util API")
public interface UtilControllerSwagger {

   @Operation(operationId = "getNowAccountingDST", summary = "Returns the local date and time accounting for DST")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Returns the local date and time accounting for DST",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = LocalDateTime.class))
   )
   @ApiResponse(
	  responseCode = ApiResponseMessages.INTERNAL_SERVER_ERROR,
	  description = "Unexpected exception occurred",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<LocalDateTime> getNowAccountingDST();
}
