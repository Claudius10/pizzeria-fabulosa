package org.clau.pizzeriadata.dao.admin;

import org.clau.pizzeriadata.dao.business.OrderRepository;

import java.time.LocalDateTime;

public interface AdminOrderRepository extends OrderRepository {

   // TODO - also by state: completed or cancelled
   int countAllByCreatedOnBetween(LocalDateTime createdOnStart, LocalDateTime createdOnEnd);
}
