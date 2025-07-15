package org.clau.pizzeriadata.dao.assets;

import org.clau.pizzeriadata.model.assets.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

   Page<Product> findAllByType(String type, Pageable pageable);
}