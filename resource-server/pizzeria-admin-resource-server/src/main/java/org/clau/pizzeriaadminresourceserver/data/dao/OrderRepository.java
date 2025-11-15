package org.clau.pizzeriaadminresourceserver.data.dao;

import org.clau.pizzeriadata.model.business.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}