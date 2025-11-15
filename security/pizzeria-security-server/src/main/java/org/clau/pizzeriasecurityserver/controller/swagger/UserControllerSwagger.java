package org.clau.pizzeriasecurityserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "User API", description = "User account related operations")
public interface UserControllerSwagger {

   @Operation(operationId = "deleteById", summary = "Delete user account")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Account deleted",
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
	  description = "Unexpected exception occurred or attempted to delete dummy account",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   ResponseEntity<?> deleteById(@PathVariable @Parameter(required = true, description = "Id of the user") Long id,
								@RequestParam @Parameter(required = true, description = "Password of the user") String password
   );

   @Operation(operationId = "passwordMatches", summary = "Check password match")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "Does not return if true, UNAUTHORIZED otherwise",
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
   ResponseEntity<?> passwordMatches(@PathVariable @Parameter(required = true, description = "Id of the user") Long id,
									 @RequestParam @Parameter(required = true, description = "Password of the user") String password
   );
}
