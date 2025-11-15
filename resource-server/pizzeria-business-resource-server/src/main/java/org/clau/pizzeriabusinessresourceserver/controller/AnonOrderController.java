package org.clau.pizzeriabusinessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessresourceserver.controller.swagger.AnonOrderControllerSwagger;
import org.clau.pizzeriabusinessresourceserver.service.AnonOrderService;
import org.clau.pizzeriabusinessresourceserver.validator.CompositeValidator;
import org.clau.pizzeriabusinessresourceserver.validator.OrderToValidate;
import org.clau.pizzeriabusinessresourceserver.validator.ValidationResult;
import org.clau.pizzeriadata.dto.business.*;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.constant.ValidationResponses;
import org.clau.pizzeriautils.util.TimeUtils;
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
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ANON + ApiRoutes.ORDER_BASE)
public class AnonOrderController implements AnonOrderControllerSwagger {

   private final AnonOrderService orderService;

   private final CompositeValidator<OrderToValidate> newOrderValidator;

   @PostMapping
   public ResponseEntity<?> createAnonOrder(
	  @RequestBody @Valid NewAnonOrderDTO newAnonOrder,
	  HttpServletRequest request) {

	  Optional<ValidationResult> validate = newOrderValidator.validate(
		 new OrderToValidate(newAnonOrder.cart(), newAnonOrder.orderDetails()));

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
