package org.clau.pizzeriadata.dao.assets;

import org.clau.pizzeriadata.model.business.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnonOrderRepository extends JpaRepository<Order, Long> {
}