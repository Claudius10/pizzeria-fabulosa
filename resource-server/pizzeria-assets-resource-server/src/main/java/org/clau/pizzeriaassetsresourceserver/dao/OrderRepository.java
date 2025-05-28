package org.clau.pizzeriaassetsresourceserver.dao;


import org.clau.pizzeriabusinessassets.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}