package org.clau.pizzeriabusinessresourceserver.service;

import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderSummaryProjection;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface OrderService {

	Optional<OrderProjection> findOrderDTOById(Long orderId);

	Order createUserOrder(Long userId, NewUserOrderDTO newUserOrder);

	void deleteUserOrderById(Long orderId);

	Page<OrderSummaryProjection> findUserOrderSummary(Long userId, int size, int page);

	// for internal use only

	Optional<CreatedOnProjection> findCreatedOnDTOById(Long orderId);
}
