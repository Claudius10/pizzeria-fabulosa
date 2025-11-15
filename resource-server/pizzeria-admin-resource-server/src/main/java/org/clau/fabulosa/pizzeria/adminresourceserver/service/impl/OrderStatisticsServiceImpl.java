package org.clau.fabulosa.pizzeria.adminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.pizzeria.adminresourceserver.data.dao.OrderStatisticsRepository;
import org.clau.fabulosa.pizzeria.adminresourceserver.data.util.Timeline;
import org.clau.fabulosa.pizzeria.adminresourceserver.function.TimelineExecutor;
import org.clau.fabulosa.pizzeria.adminresourceserver.service.OrderStatisticsService;
import org.clau.fabulosa.utils.enums.OrderState;
import org.clau.fabulosa.utils.enums.UserState;
import org.clau.fabulosa.utils.util.TimeUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderStatisticsServiceImpl implements OrderStatisticsService {

   private final OrderStatisticsRepository orderStatisticsRepository;

   @Override
   public List<Integer> findCountByOrderState(String timelineStr, OrderState state) {
	  Timeline timeline = Timeline.fromString(timelineStr);
	  String orderState = state.toString();

	  return TimelineExecutor.execute(
		 timeline,
		 TimeUtils::getNowAccountingDST,
		 interval -> orderStatisticsRepository.countAllByCreatedOnBetweenAndState(interval.start(), interval.end(), orderState)
	  );
   }

   @Override
   public List<Integer> findCountByUserState(String timelineStr, UserState state) {
	  Timeline timeline = Timeline.fromString(timelineStr);

	  return switch (state) {
		 case REGISTERED -> TimelineExecutor.execute(
			timeline,
			TimeUtils::getNowAccountingDST,
			interval -> orderStatisticsRepository.countAllByCreatedOnBetweenAndUserIdIsNotNull(interval.start(), interval.end())
		 );
		 case ANONYMOUS -> TimelineExecutor.execute(
			timeline,
			TimeUtils::getNowAccountingDST,
			interval -> orderStatisticsRepository.countAllByCreatedOnBetweenAndUserIdIsNull(interval.start(), interval.end())
		 );
	  };
   }

   @Override
   public Integer findCountAllByUserState(UserState state) {
	  return switch (state) {
		 case REGISTERED -> orderStatisticsRepository.countAllByUserIdIsNotNull();
		 case ANONYMOUS -> orderStatisticsRepository.countAllByUserIdIsNull();
	  };
   }
}
