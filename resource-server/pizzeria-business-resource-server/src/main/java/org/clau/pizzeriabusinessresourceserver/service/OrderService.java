package org.clau.pizzeriabusinessresourceserver.service;

import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {

   Optional<Order> findById(Long orderId);

   Order create(Long userId, NewUserOrderDTO newUserOrder);

   void deleteById(Long orderId);

   Page<Order> findSummary(Long userId, int size, int page);

   // for internal use only

   Optional<CreatedOnProjection> findCreatedOnById(Long orderId);
}
