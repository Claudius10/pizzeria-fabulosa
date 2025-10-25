package org.clau.pizzeriadata.dao.admin;

import org.clau.pizzeriadata.dao.business.OrderRepository;

import java.time.LocalDateTime;

public interface AdminOrderRepository extends OrderRepository {

   int countAllByCreatedOnBetweenAndState(LocalDateTime createdOnStart, LocalDateTime createdOnEnd, String state);
}
