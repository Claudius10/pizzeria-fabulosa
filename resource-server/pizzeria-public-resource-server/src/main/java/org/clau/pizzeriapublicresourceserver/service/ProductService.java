package org.clau.pizzeriapublicresourceserver.service;

import org.clau.pizzeriapublicassets.model.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

   Page<Product> findAllByType(String type, int size, int page);
}
