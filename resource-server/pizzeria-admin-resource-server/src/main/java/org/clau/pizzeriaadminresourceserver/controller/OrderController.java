package org.clau.pizzeriaadminresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriaadminresourceserver.service.AdminOrderService;
import org.clau.pizzeriautils.constant.common.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ADMIN_BASE + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

   private final AdminOrderService orderService;

   @GetMapping(Route.COUNT)
   public ResponseEntity<List<Integer>> findCountForTimeline(@RequestParam String timeline) {
	  return ResponseEntity.ok(orderService.findCountForTimeline(timeline));
   }
}
