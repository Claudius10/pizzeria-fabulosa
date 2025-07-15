package org.clau.pizzeriadata.dao.business;

import org.clau.pizzeriadata.dao.business.projection.CreatedOnProjection;
import org.clau.pizzeriadata.model.business.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

   Page<Order> findAllByUserId(Long userId, Pageable pageable);

   Optional<CreatedOnProjection> findCreatedOnById(Long orderId);
}