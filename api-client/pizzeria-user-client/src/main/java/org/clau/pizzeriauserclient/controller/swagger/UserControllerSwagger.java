package org.clau.pizzeriauserclient.controller.swagger;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.UserInfoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Mono;

@Tag(name = "User API", description = "User account related operations")
public interface UserControllerSwagger {

	@Operation(operationId = "getUserInfo", summary = "Find user info")
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
	ResponseEntity<?> getUserInfo(OAuth2AuthorizedClient authorizedClient);

	@Operation(operationId = "deleteById", summary = "Delete user account")
	@ApiResponse(
			responseCode = Response.OK,
			description = "Account deleted",
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
			description = "Unexpected exception occurred or attempted to delete dummy account",
			content = @Content(mediaType = Response.JSON, schema = @Schema(implementation = ResponseDTO.class))
	)
	Mono<ResponseEntity<Object>> deleteById(
			OAuth2AuthorizedClient authorizedClient,
			@RequestParam @Parameter(required = true, description = "Id of the user account to delete") Long id);

}
