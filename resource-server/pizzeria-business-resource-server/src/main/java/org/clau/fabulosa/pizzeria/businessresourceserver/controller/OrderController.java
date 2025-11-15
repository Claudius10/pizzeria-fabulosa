package org.clau.fabulosa.pizzeria.businessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.dto.business.*;
import org.clau.fabulosa.pizzeria.businessresourceserver.controller.swagger.OrderControllerSwagger;
import org.clau.fabulosa.pizzeria.businessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.fabulosa.pizzeria.businessresourceserver.service.OrderService;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.CompositeValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.OrderToValidate;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.ValidationResult;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.data.model.business.Order;
import org.clau.fabulosa.data.model.common.APIError;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.clau.fabulosa.utils.constant.MyApps;
import org.clau.fabulosa.utils.constant.ValidationResponses;
import org.clau.fabulosa.utils.util.TimeUtils;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.ORDER_BASE)
public class OrderController implements OrderControllerSwagger {

   private final OrderService orderService;

   private final CompositeValidator<OrderToValidate> newOrderValidator;

   private final Validator<LocalDateTime> cancelOrderValidator;

   @PostMapping
   public ResponseEntity<?> create(
	  @RequestBody @Valid NewUserOrderDTO order,
	  @RequestParam(name = ApiRoutes.PARAM_USER_ID) Long userId,
	  HttpServletRequest request) {

	  Optional<ValidationResult> validate = newOrderValidator.validate(new OrderToValidate(order.cart(), order.orderDetails()));

	  if (validate.isPresent()) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ValidationResponses.ORDER_VALIDATION_FAILED)
			   .withOrigin(MyApps.RESOURCE_SERVER_BUSINESS)
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
		 createdOrder.getState(),
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

   @GetMapping(ApiRoutes.ORDER_ID)
   public ResponseEntity<OrderDTO> findById(@PathVariable Long orderId) {

	  Optional<Order> order = orderService.findById(orderId);

	  return order.map(theOrder -> ResponseEntity.ok().body(
			new OrderDTO(
			   theOrder.getId(),
			   theOrder.getFormattedCreatedOn(),
			   theOrder.getState(),
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

   @PutMapping(ApiRoutes.ORDER_ID)
   public ResponseEntity<?> cancelById(@PathVariable Long orderId, HttpServletRequest request) {

	  Optional<CreatedOnProjection> createdOnDTOById = orderService.findCreatedOnById(orderId);

	  if (createdOnDTOById.isEmpty()) {
		 return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	  } else {
		 ValidationResult validate = cancelOrderValidator.validate(createdOnDTOById.get().getCreatedOn());
		 if (!validate.valid()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
			   .apiError(APIError.builder()
				  .withId(UUID.randomUUID().getMostSignificantBits())
				  .withCreatedOn(TimeUtils.getNowAccountingDST())
				  .withCause(ValidationResponses.ORDER_VALIDATION_FAILED)
				  .withOrigin(MyApps.RESOURCE_SERVER_BUSINESS)
				  .withPath(request.getPathInfo())
				  .withMessage(validate.message())
				  .withLogged(false)
				  .withFatal(false)
				  .build())
			   .status(HttpStatus.BAD_REQUEST.value())
			   .build());
		 }
	  }

	  orderService.cancelById(orderId);
	  return ResponseEntity.ok(orderId);
   }

   @GetMapping(ApiRoutes.ORDER_SUMMARY)
   public ResponseEntity<OrderSummaryListDTO> findSummary(
	  @RequestParam(name = ApiRoutes.PARAM_PAGE_NUMBER) Integer pageNumber,
	  @RequestParam(name = ApiRoutes.PARAM_PAGE_SIZE) Integer pageSize,
	  @RequestParam(name = ApiRoutes.PARAM_USER_ID) Long userId) {

	  Page<Order> summary = orderService.findSummary(userId, pageSize, pageNumber);

	  OrderSummaryListDTO orderSummaryListDTO = new OrderSummaryListDTO(
		 summary.getContent().stream().map(order ->
			new OrderSummaryDTO(
			   order.getId(),
			   order.getFormattedCreatedOn(),
			   order.getState(),
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
