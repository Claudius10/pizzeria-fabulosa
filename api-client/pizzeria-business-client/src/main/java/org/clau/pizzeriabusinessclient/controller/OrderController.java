package org.clau.pizzeriabusinessclient.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.Security;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessclient.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriabusinessclient.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.WebUtils;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

	private final OrderService orderService;

	@Override
	public Mono<ResponseEntity<Object>> create(NewUserOrderDTO order, Long userId, HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, Security.ACCESS_TOKEN);

		Mono<ResponseEntity<Object>> result = orderService.create(userId, order, accessToken.getValue()).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}

	@Override
	public Mono<ResponseEntity<Object>> findById(Long orderId, HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, Security.ACCESS_TOKEN);

		Mono<ResponseEntity<Object>> result = orderService.findById(orderId, accessToken.getValue()).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}

	@Override
	public Mono<ResponseEntity<Object>> deleteById(Long orderId, HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, Security.ACCESS_TOKEN);

		Mono<ResponseEntity<Object>> result = orderService.deleteById(orderId, accessToken.getValue()).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}

	@Override
	public Mono<ResponseEntity<Object>> findSummary(Integer pageNumber, Integer pageSize, Long userId, HttpServletRequest request) {
		Cookie accessToken = WebUtils.getCookie(request, Security.ACCESS_TOKEN);

		Mono<ResponseEntity<Object>> result = orderService.findSummary(userId, pageSize, pageNumber, accessToken.getValue()).map(response -> {
			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).body(response);
			}
		});

		return result;
	}
}
