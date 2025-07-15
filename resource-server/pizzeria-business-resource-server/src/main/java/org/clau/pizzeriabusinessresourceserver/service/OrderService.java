package org.clau.pizzeriabusinessresourceserver.service;

import org.clau.pizzeriadata.dao.business.projection.CreatedOnProjection;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriautils.dto.business.NewUserOrderDTO;
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
