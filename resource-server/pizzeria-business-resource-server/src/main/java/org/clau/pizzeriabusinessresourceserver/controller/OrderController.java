package org.clau.pizzeriabusinessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriabusinessresourceserver.controller.swagger.OrderControllerSwagger;
import org.clau.pizzeriabusinessresourceserver.service.OrderService;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
import org.clau.pizzeriadata.dao.business.projection.CreatedOnProjection;
import org.clau.pizzeriadata.model.business.Order;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.dto.business.*;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.validation.business.order.CompositeValidator;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.clau.pizzeriautils.validation.business.order.Validator;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

   private final OrderService orderService;

   private final CompositeValidator<NewOrder> newOrderValidator;

   private final Validator<LocalDateTime> deleteOrderValidator;

   @PostMapping
   public ResponseEntity<?> create(
	  @RequestBody @Valid NewUserOrderDTO order,
	  @RequestParam(name = Route.USER_ID_PARAM) Long userId,
	  HttpServletRequest request) {

	  Optional<ValidationResult> validate = newOrderValidator.validate(new NewOrder(order.cart(), order.orderDetails()));

	  if (validate.isPresent()) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ValidationResponses.ORDER_VALIDATION_FAILED)
			   .withOrigin(Constant.APP_NAME)
			   .withPath(request.getPathInfo())
			   .withMessage(validate.get().message())
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }

	  Order createdOrder = orderService.create(userId, order);

	  CreatedOrderDTO createdOrderDTO = new CreatedOrderDTO(
		 createdOrder.getId(),
		 createdOrder.getFormattedCreatedOn(),
		 null,
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
		 )
	  );

	  return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderDTO);
   }

   @GetMapping(Route.ORDER_ID)
   public ResponseEntity<OrderDTO> findById(@PathVariable Long orderId) {

	  Optional<Order> order = orderService.findById(orderId);

	  return order.map(theOrder -> ResponseEntity.ok().body(
			new OrderDTO(
			   theOrder.getId(),
			   theOrder.getFormattedCreatedOn(),
			   theOrder.getAddress(),
			   new OrderDetailsDTO(
				  theOrder.getOrderDetails().getDeliveryTime(),
				  theOrder.getOrderDetails().getPaymentMethod(),
				  theOrder.getOrderDetails().getBillToChange(),
				  theOrder.getOrderDetails().getComment(),
				  theOrder.getOrderDetails().getStorePickUp(),
				  theOrder.getOrderDetails().getChangeToGive()
			   ),
			   new CartDTO(
				  theOrder.getCart().getTotalQuantity(),
				  theOrder.getCart().getTotalCost(),
				  theOrder.getCart().getTotalCostOffers(),
				  theOrder.getCart().getCartItems().stream().map(item -> new CartItemDTO(
					 item.getId(),
					 item.getType(),
					 item.getPrice(),
					 item.getQuantity(),
					 item.getName(),
					 item.getDescription(),
					 item.getFormats()
				  )).toList()
			   )
			)
		 ))
		 .orElse(ResponseEntity.noContent().build());
   }

   @DeleteMapping(Route.ORDER_ID)
   public ResponseEntity<?> deleteById(@PathVariable Long orderId, HttpServletRequest request) {

	  Optional<CreatedOnProjection> createdOnDTOById = orderService.findCreatedOnById(orderId);

	  if (createdOnDTOById.isEmpty()) {
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	  } else {
		 ValidationResult validate = deleteOrderValidator.validate(createdOnDTOById.get().getCreatedOn());
		 if (!validate.valid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
			   .apiError(APIError.builder()
				  .withId(UUID.randomUUID().getMostSignificantBits())
				  .withCreatedOn(TimeUtils.getNowAccountingDST())
				  .withCause(ValidationResponses.ORDER_VALIDATION_FAILED)
				  .withOrigin(Constant.APP_NAME)
				  .withPath(request.getPathInfo())
				  .withMessage(validate.message())
				  .withLogged(false)
				  .withFatal(false)
				  .build())
			   .status(HttpStatus.BAD_REQUEST.value())
			   .build());
		 }
	  }

	  orderService.deleteById(orderId);
	  return ResponseEntity.ok(orderId);
   }

   @GetMapping(Route.ORDER_SUMMARY)
   public ResponseEntity<OrderSummaryListDTO> findSummary(
	  @RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
	  @RequestParam(name = Route.PAGE_SIZE) Integer pageSize,
	  @RequestParam(name = Route.USER_ID_PARAM) Long userId) {

	  Page<Order> summary = orderService.findSummary(userId, pageSize, pageNumber);

	  OrderSummaryListDTO orderSummaryListDTO = new OrderSummaryListDTO(
		 summary.getContent().stream().map(order ->
			new OrderSummaryDTO(
			   order.getId(),
			   order.getFormattedCreatedOn(),
			   order.getOrderDetails().getPaymentMethod(),
			   order.getCart().getTotalQuantity(),
			   order.getCart().getTotalCost(),
			   order.getCart().getTotalCostOffers()
			)).toList(),
		 summary.getNumber(),
		 summary.getSize(),
		 summary.getTotalElements(),
		 summary.isLast()
	  );

	  return ResponseEntity.ok(orderSummaryListDTO);
   }
}
