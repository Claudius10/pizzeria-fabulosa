package org.clau.pizzeriauserclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.UserInfoDTO;
import org.clau.pizzeriauserclient.controller.swagger.UserControllerSwagger;
import org.clau.pizzeriauserclient.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.USER_BASE)
public class UserController implements UserControllerSwagger {

	private final UserService userService;

	@Override
	@GetMapping
	public ResponseEntity<?> getUserInfo(@RegisteredOAuth2AuthorizedClient("user-client") OAuth2AuthorizedClient authorizedClient) {
		Map<String, Object> userInfoClaims = userService.getInfo(authorizedClient).block();

		UserInfoDTO userInfoDTO = new UserInfoDTO(
				String.valueOf(userInfoClaims.get("id")),
				(String) userInfoClaims.get("sub"),
				(String) userInfoClaims.get("name"),
				(String) userInfoClaims.get("email"),
				(Boolean) userInfoClaims.get("email_verified"),
				String.valueOf(userInfoClaims.get("phone_number")),
				(Boolean) userInfoClaims.get("phone_number_verified"),
				(String) userInfoClaims.get("address"),
				(String) userInfoClaims.get("locale"),
				(String) userInfoClaims.get("zoneinfo"),
				(String) userInfoClaims.get("updated_at")
		);

		return ResponseEntity.ok(userInfoDTO);
	}

	@Override
	@DeleteMapping(Route.USER_ID)
	public Mono<ResponseEntity<Object>> deleteById(@RegisteredOAuth2AuthorizedClient("user-client") OAuth2AuthorizedClient authorizedClient,
												   @PathVariable Long userId) {

		Mono<ResponseEntity<Object>> result = userService.deleteById(userId, authorizedClient).map(response -> {

			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.ok().build();
			}
		});

		return result;
	}

	@GetMapping("/logged-in")
	public void userIn() {

	}

	@GetMapping("/logged-out")
	public void userOut() {

	}
}
