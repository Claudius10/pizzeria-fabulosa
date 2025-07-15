package org.clau.pizzeriapublicresourceserver.service;

import org.clau.pizzeriadata.model.assets.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

   Page<Product> findAllByType(String type, int size, int page);
}
