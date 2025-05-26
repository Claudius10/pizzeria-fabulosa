package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriaassetsclient.controller.swagger.ProductControllerSwagger;
import org.clau.pizzeriaassetsclient.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.V1 + Route.PRODUCT_BASE)
public class ProductController implements ProductControllerSwagger {

	private final ProductService productService;

	@GetMapping
	public Mono<ResponseEntity<Object>> findAllByType(
			@RequestParam String type,
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize) {

		Mono<ResponseEntity<Object>> result = productService.findAllByType(type, pageSize, pageNumber).map(response -> {
			if (response instanceof ResponseDTO) {
				return ResponseEntity.internalServerError().body(response);
			} else {
				return ResponseEntity.ok(response);
			}
		});

		return result;
	}
}
