package org.clau.pizzeriaadminresourceserver.data.dao;

import java.time.LocalDateTime;

public interface OrderStatisticsRepository extends OrderRepository {

   int countAllByCreatedOnBetweenAndState(LocalDateTime createdOnStart, LocalDateTime createdOnEnd, String state);

   int countAllByCreatedOnBetweenAndUserIdIsNull(LocalDateTime createdOnStart, LocalDateTime createdOnEnd);

   int countAllByCreatedOnBetweenAndUserIdIsNotNull(LocalDateTime createdOnStart, LocalDateTime createdOnEnd);

   int countAllByUserIdIsNull();

   int countAllByUserIdIsNotNull();
}
