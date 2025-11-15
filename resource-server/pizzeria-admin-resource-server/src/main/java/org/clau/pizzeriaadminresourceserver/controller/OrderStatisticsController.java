package org.clau.pizzeriaadminresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.controller.swagger.OrderStatisticsControllerSwagger;
import org.clau.pizzeriaadminresourceserver.service.OrderStatisticsService;
import org.clau.pizzeriadata.dto.admin.OrderStatistics;
import org.clau.pizzeriadata.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.enums.UserState;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ADMIN + ApiRoutes.ORDER_BASE + ApiRoutes.STATISTICS)
public class OrderStatisticsController implements OrderStatisticsControllerSwagger {

   private final OrderStatisticsService orderService;

   @GetMapping(ApiRoutes.STATE + ApiRoutes.ORDER_BASE)
   public ResponseEntity<OrderStatistics> findCountByOrderState(@RequestParam String timeline) {
	  OrderStatistics orderStatistics = new OrderStatistics(
		 List.of(
			new OrderStatisticsByState(orderService.findCountByOrderState(timeline, OrderState.COMPLETED)),
			new OrderStatisticsByState(orderService.findCountByOrderState(timeline, OrderState.CANCELLED)))
	  );

	  return ResponseEntity.ok(orderStatistics);
   }

   @GetMapping(ApiRoutes.STATE + ApiRoutes.USER_BASE)
   public ResponseEntity<OrderStatistics> findCountByUserState(@RequestParam String timeline) {
	  OrderStatistics orderStatistics;

	  if (timeline.equals("all")) {
		 orderStatistics = new OrderStatistics(
			List.of(
			   new OrderStatisticsByState(List.of(orderService.findCountAllByUserState(UserState.REGISTERED))),
			   new OrderStatisticsByState(List.of(orderService.findCountAllByUserState(UserState.ANONYMOUS)))
			));
	  } else {
		 orderStatistics = new OrderStatistics(
			List.of(
			   new OrderStatisticsByState(orderService.findCountByUserState(timeline, UserState.REGISTERED)),
			   new OrderStatisticsByState(orderService.findCountByUserState(timeline, UserState.ANONYMOUS))
			));
	  }

	  return ResponseEntity.ok(orderStatistics);
   }
}
