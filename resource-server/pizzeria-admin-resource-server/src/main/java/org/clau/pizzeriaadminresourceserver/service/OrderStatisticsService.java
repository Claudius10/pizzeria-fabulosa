package org.clau.pizzeriaadminresourceserver.service;

import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.enums.UserState;

import java.util.List;

public interface OrderStatisticsService {

   List<Integer> findCountByOrderState(String timelineStr, OrderState state);

   List<Integer> findCountByUserState(String timelineStr, UserState state);

   Integer findCountAllByUserState(UserState state);
}
