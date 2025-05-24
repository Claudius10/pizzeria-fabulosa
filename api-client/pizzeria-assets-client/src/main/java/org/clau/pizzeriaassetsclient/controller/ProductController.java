package org.clau.pizzeriaassetsclient.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriaassetsclient.controller.swagger.ProductControllerSwagger;
import org.clau.pizzeriaassetsclient.dto.ProductListDTO;
import org.clau.pizzeriastoreassets.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.PRODUCT_BASE + Route.V1)
public class ProductController implements ProductControllerSwagger {

	@GetMapping
	public ResponseEntity<ProductListDTO> findAllByType(
			@RequestParam String type,
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize) {

		Page<Product> allProductsByType = null;

		ProductListDTO productListDTO = new ProductListDTO(
				allProductsByType.getContent(),
				allProductsByType.getTotalPages(),
				allProductsByType.getPageable().getPageSize(),
				allProductsByType.getTotalElements(),
				allProductsByType.hasNext()
		);

		return ResponseEntity.ok(productListDTO);
	}
}
