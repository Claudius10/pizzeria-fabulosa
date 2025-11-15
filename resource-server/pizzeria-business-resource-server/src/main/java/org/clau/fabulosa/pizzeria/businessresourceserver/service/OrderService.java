package org.clau.fabulosa.pizzeria.businessresourceserver.service;

import org.clau.fabulosa.pizzeria.businessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.fabulosa.data.dto.business.NewUserOrderDTO;
import org.clau.fabulosa.data.model.business.Order;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {

   Optional<Order> findById(Long orderId);

   Order create(Long userId, NewUserOrderDTO newUserOrder);

   void cancelById(Long orderId);

   Page<Order> findSummary(Long userId, int size, int page);

   // for internal use only

   Optional<CreatedOnProjection> findCreatedOnById(Long orderId);
}
