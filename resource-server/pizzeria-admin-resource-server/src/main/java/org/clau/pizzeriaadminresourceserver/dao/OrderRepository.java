package org.clau.pizzeriaadminresourceserver.dao;

import org.clau.pizzeriadata.model.business.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}