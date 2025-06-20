package org.clau.pizzeriauserresourceserver.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.UserInfoDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User Account API", description = "User account related operations")
@RestController
public class UserInfoController {

   @Operation(operationId = "getUserInfo", summary = "Find user account info")
   @ApiResponse(
	  responseCode = Response.OK,
	  description = "The user info",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = UserInfoDTO.class))
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
	  description = "Unexpected exception occurred or attempted to delete dummy account",
	  content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
   )
   @GetMapping("/userinfo")
   void getUserInfo() {
	  // API definition only
   }
}
