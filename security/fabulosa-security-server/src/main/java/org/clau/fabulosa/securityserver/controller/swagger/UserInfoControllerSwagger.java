package org.clau.fabulosa.securityserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.securityserver.data.dto.UserInfoDTO;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User API", description = "User account related operations")
@RestController
public class UserInfoControllerSwagger {

   @Operation(operationId = "getUserInfo", summary = "Find user account info")
   @ApiResponse(
	  responseCode = ApiResponseMessages.OK,
	  description = "The user info",
	  content = @Content(mediaType = ApiResponseMessages.JSON, schema = @Schema(implementation = UserInfoDTO.class))
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
   @GetMapping("/userinfo")
   void getUserInfo() {
	  // API definition only
   }
}
