package org.clau.fabulosa.pizzeria.publicresourceserver.service;

import org.clau.fabulosa.data.model.assets.Product;
import org.springframework.data.domain.Page;

public interface ProductService {

   Page<Product> findAllByType(String type, int size, int page);
}
