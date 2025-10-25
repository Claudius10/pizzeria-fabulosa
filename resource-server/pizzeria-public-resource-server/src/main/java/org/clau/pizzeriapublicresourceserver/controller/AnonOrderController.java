package org.clau.pizzeriapublicresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriapublicresourceserver.controller.swagger.AnonOrderControllerSwagger;
import org.clau.pizzeriapublicresourceserver.service.AnonOrderService;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.dto.business.*;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.util.common.constant.MyApps;
import org.clau.pizzeriautils.validation.business.order.CompositeValidator;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ANON_BASE + Route.ORDER_BASE)
public class AnonOrderController implements AnonOrderControllerSwagger {

   private final AnonOrderService orderService;

   private final CompositeValidator<NewOrder> newOrderValidator;

   @PostMapping
   public ResponseEntity<?> createAnonOrder(
	  @RequestBody @Valid NewAnonOrderDTO newAnonOrder,
	  HttpServletRequest request) {

	  Optional<ValidationResult> validate = newOrderValidator.validate(
		 new NewOrder(newAnonOrder.cart(), newAnonOrder.orderDetails()));

	  if (validate.isPresent()) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ValidationResponses.ORDER_VALIDATION_FAILED)
			   .withOrigin(MyApps.RESOURCE_SERVER_PUBLIC)
			   .withPath(request.getPathInfo())
			   .withMessage(validate.get().message())
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }

	  Order createdOrder = orderService.createAnonOrder(newAnonOrder);

	  CreatedOrderDTO createdOrderDTO = new CreatedOrderDTO(
		 createdOrder.getId(),
		 createdOrder.getFormattedCreatedOn(),
		 createdOrder.getState(),
		 new CustomerDTO(
			createdOrder.getAnonCustomerName(),
			createdOrder.getAnonCustomerContactNumber(),
			createdOrder.getAnonCustomerEmail()
		 ),
		 createdOrder.getAddress(),
		 new OrderDetailsDTO(
			createdOrder.getOrderDetails().getDeliveryTime(),
			createdOrder.getOrderDetails().getPaymentMethod(),
			createdOrder.getOrderDetails().getBillToChange(),
			createdOrder.getOrderDetails().getComment(),
			createdOrder.getOrderDetails().getStorePickUp(),
			createdOrder.getOrderDetails().getChangeToGive()
		 ),
		 new CartDTO(
			createdOrder.getCart().getTotalQuantity(),
			createdOrder.getCart().getTotalCost(),
			createdOrder.getCart().getTotalCostOffers(),
			createdOrder.getCart().getCartItems().stream().map(item -> new CartItemDTO(
			   item.getId(),
			   item.getType(),
			   item.getPrice(),
			   item.getQuantity(),
			   item.getName(),
			   item.getDescription(),
			   item.getFormats()
			)).toList()
		 ));

	  return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDTO);
   }
}
