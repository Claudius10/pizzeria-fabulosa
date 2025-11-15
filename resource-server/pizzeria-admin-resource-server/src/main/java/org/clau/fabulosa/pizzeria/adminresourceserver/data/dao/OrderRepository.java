package org.clau.fabulosa.pizzeria.adminresourceserver.data.dao;

import org.clau.fabulosa.data.model.business.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}