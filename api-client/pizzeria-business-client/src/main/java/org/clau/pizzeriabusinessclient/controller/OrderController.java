package org.clau.pizzeriabusinessclient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessclient.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriabusinessclient.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.*;

import static org.clau.pizzeriabusinessclient.util.Constant.AUTHORIZATION_CODE;
import static org.clau.pizzeriabusinessclient.util.Constant.CLIENT_NAME;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

	private final OrderService orderService;

	@PostMapping(params = AUTHORIZATION_CODE)
	public ResponseEntity<?> create(
			@RequestBody @Valid NewUserOrderDTO order,
			@RequestParam Long userId,
			@RegisteredOAuth2AuthorizedClient(CLIENT_NAME) OAuth2AuthorizedClient authorizedClient) {

		Object response = orderService.create(userId, order, authorizedClient).block();

		if (response instanceof ResponseDTO responseDTO) {
			return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
		} else {
			return ResponseEntity.status(HttpStatus.CREATED).body(response);
		}
	}

	@GetMapping(value = Route.ORDER_ID, params = AUTHORIZATION_CODE)
	public ResponseEntity<?> findById(
			@PathVariable Long orderId,
			@RegisteredOAuth2AuthorizedClient(CLIENT_NAME) OAuth2AuthorizedClient authorizedClient) {

		Object response = orderService.findById(orderId, authorizedClient).block();

		if (response instanceof ResponseDTO responseDTO) {
			return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@DeleteMapping(value = Route.ORDER_ID, params = AUTHORIZATION_CODE)
	public ResponseEntity<?> deleteById(
			@PathVariable Long orderId,
			@RegisteredOAuth2AuthorizedClient(CLIENT_NAME) OAuth2AuthorizedClient authorizedClient) {

		Object response = orderService.deleteById(orderId, authorizedClient).block();

		if (response instanceof ResponseDTO responseDTO) {
			return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}

	@GetMapping(value = Route.ORDER_SUMMARY)
	public ResponseEntity<?> findSummary(
			@RequestParam(name = "grant_type") String grantType,
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize,
			@RequestParam Long userId,
			@RegisteredOAuth2AuthorizedClient(CLIENT_NAME) OAuth2AuthorizedClient authorizedClient) {

		Object response = orderService.findSummary(userId, pageSize, pageNumber, authorizedClient).block();

		if (response instanceof ResponseDTO responseDTO) {
			return ResponseEntity.status(responseDTO.getStatus()).body(responseDTO);
		} else {
			return ResponseEntity.status(HttpStatus.OK).body(response);
		}
	}
}
