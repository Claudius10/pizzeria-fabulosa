package org.clau.pizzeriabusinessresourceserver.dao;

import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.pizzeriadata.model.business.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

   Page<Order> findAllByUserId(Long userId, Pageable pageable);

   @Modifying
   @Query("update Order o set o.state = :state where o.id = :orderId")
   void updateState(Long orderId, String state);

   Optional<CreatedOnProjection> findCreatedOnById(Long orderId);
}