package org.clau.pizzeriaadminresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriaadminresourceserver.service.AdminOrderService;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.business.OrderState;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.dto.admin.OrderStatisticsByState;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.util.common.constant.MyApps;
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
@RequestMapping(Route.API + Route.V1 + Route.ADMIN_BASE + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

   private final AdminOrderService orderService;

   @GetMapping(Route.COUNT)
   public ResponseEntity<?> findCountForTimelineAndState(HttpServletRequest request, @RequestParam String timeline, @RequestParam String state) {
	  try {
		 String extractedState = OrderState.valueOf(state).toString();
		 List<Integer> countForTimelineAndState = orderService.findCountForTimelineAndState(timeline, extractedState);
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
}
