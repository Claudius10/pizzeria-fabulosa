package org.clau.pizzeriapublicresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.model.assets.Product;
import org.clau.pizzeriapublicresourceserver.controller.swagger.ProductControllerSwagger;
import org.clau.pizzeriapublicresourceserver.service.ProductService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.assets.ProductListDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.RESOURCE + Route.PRODUCT_BASE)
public class ProductController implements ProductControllerSwagger {

   private final ProductService productService;

   @GetMapping
   public ResponseEntity<ProductListDTO> findAllByType(
	  @RequestParam(name = Route.PRODUCT_TYPE) String type,
	  @RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
	  @RequestParam(name = Route.PAGE_SIZE) Integer pageSize) {

	  Page<Product> allByType = productService.findAllByType(type, pageSize, pageNumber);

	  ProductListDTO productListDTO = new ProductListDTO(
		 allByType.getContent(),
		 allByType.getNumber(),
		 allByType.getSize(),
		 allByType.getTotalElements(),
		 allByType.isLast()
	  );

	  return ResponseEntity.ok(productListDTO);
   }
}
