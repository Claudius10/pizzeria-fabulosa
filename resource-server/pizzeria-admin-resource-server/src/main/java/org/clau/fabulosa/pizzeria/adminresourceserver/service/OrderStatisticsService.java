package org.clau.fabulosa.pizzeria.adminresourceserver.service;

import org.clau.fabulosa.utils.enums.OrderState;
import org.clau.fabulosa.utils.enums.UserState;

import java.util.List;

public interface OrderStatisticsService {

   List<Integer> findCountByOrderState(String timelineStr, OrderState state);

   List<Integer> findCountByUserState(String timelineStr, UserState state);

   Integer findCountAllByUserState(UserState state);
}
