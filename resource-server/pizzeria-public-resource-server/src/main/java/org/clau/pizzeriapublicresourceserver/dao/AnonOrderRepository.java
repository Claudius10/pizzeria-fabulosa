package org.clau.pizzeriapublicresourceserver.dao;

import org.clau.pizzeriabusinessassets.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonOrderRepository extends JpaRepository<Order, Long> {
}
