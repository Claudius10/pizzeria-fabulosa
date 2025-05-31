package org.clau.pizzeriabusinessresourceserver.dao;

import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

	Page<Order> findAllByUserId(Long userId, Pageable pageable);

	Optional<CreatedOnProjection> findCreatedOnById(Long orderId);
}