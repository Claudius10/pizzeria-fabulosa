package org.clau.pizzeriaassetsresourceserver.service;

import org.clau.pizzeriastoreassets.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

   Page<Product> findAllByType(String type, int size, int page);
}
