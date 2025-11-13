package org.clau.pizzeriaadminresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.controller.swagger.OrderStatisticsControllerSwagger;
import org.clau.pizzeriaadminresourceserver.service.OrderStatisticsService;
import org.clau.pizzeriadata.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.constant.ValidationResponses;
import org.clau.pizzeriautils.enums.OrderState;
import org.clau.pizzeriautils.enums.UserState;
import org.clau.pizzeriautils.util.TimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ADMIN + ApiRoutes.ORDER_BASE + ApiRoutes.STATISTICS + ApiRoutes.STATE)
public class OrderStatisticsController implements OrderStatisticsControllerSwagger {

   private final OrderStatisticsService orderService;

   @GetMapping(ApiRoutes.ORDER_BASE)
   public ResponseEntity<?> findCountByOrderState(HttpServletRequest request, @RequestParam String timeline, @RequestParam String state) {
	  try {
		 OrderState orderState = OrderState.valueOf(state);
		 List<Integer> countForTimelineAndState = orderService.findCountByOrderState(timeline, orderState);
		 return ResponseEntity.ok(new OrderStatisticsByState(countForTimelineAndState));
	  } catch (IllegalArgumentException ex) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ValidationResponses.ORDER_STATE_UNKNOWN)
			   .withOrigin(MyApps.RESOURCE_SERVER_ADMIN)
			   .withPath(request.getPathInfo())
			   .withMessage("Supported states: " + Arrays.toString(OrderState.values()))
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }
   }

   @GetMapping(ApiRoutes.USER_BASE)
   public ResponseEntity<?> findCountByUserState(HttpServletRequest request, @RequestParam String timeline, @RequestParam String state) {
	  try {
		 UserState userState = UserState.valueOf(state);
		 List<Integer> countForTimelineAndState = orderService.findCountByUserState(timeline, userState);
		 return ResponseEntity.ok(new OrderStatisticsByState(countForTimelineAndState));
	  } catch (IllegalArgumentException ex) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ValidationResponses.USER_STATE_UNKNOWN)
			   .withOrigin(MyApps.RESOURCE_SERVER_ADMIN)
			   .withPath(request.getPathInfo())
			   .withMessage("Supported states: " + Arrays.toString(UserState.values()))
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }
   }
}
