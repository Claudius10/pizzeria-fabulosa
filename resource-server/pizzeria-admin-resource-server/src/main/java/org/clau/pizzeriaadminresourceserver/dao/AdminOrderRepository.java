package org.clau.pizzeriaadminresourceserver.dao;

import java.time.LocalDateTime;

public interface AdminOrderRepository extends OrderRepository {

   int countAllByCreatedOnBetweenAndState(LocalDateTime createdOnStart, LocalDateTime createdOnEnd, String state);
}
