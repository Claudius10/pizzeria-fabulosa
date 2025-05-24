package org.clau.pizzeriaassetsresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsresourceserver.service.ProductService;
import org.clau.pizzeriastoreassets.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.PRODUCT_BASE + Route.V1)
public class ProductController {

	private final ProductService productService;

	@GetMapping
	public Page<Product> findAllByType(
			@RequestParam String type,
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize) {

		return productService.findAllByType(type, pageNumber, pageSize);
	}
}
