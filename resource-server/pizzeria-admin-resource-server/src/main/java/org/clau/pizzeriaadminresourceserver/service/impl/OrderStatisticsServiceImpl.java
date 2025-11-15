package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.data.dao.OrderStatisticsRepository;
import org.clau.pizzeriaadminresourceserver.data.util.Timeline;
import org.clau.pizzeriaadminresourceserver.function.TimelineExecutor;
import org.clau.pizzeriaadminresourceserver.service.OrderStatisticsService;
import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.enums.UserState;
import org.clau.pizzeriautils.util.TimeUtils;
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
