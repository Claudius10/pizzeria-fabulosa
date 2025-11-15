package org.clau.fabulosa.pizzeria.publicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.model.assets.Product;
import org.clau.fabulosa.pizzeria.publicresourceserver.dao.ProductRepository;
import org.clau.fabulosa.pizzeria.publicresourceserver.service.ProductService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Transactional
public class ProductServiceImpl implements ProductService {

   private final ProductRepository productRepository;

   @Override
   @Cacheable("findAllByType")
   public Page<Product> findAllByType(String productType, int size, int page) {

	  Sort.TypedSort<Product> product = Sort.sort(Product.class);
	  Sort sort = product.by(Product::getId).descending();
	  PageRequest pageRequest = PageRequest.of(page, size, sort);

	  return productRepository.findAllByType(productType, pageRequest);
   }
}
