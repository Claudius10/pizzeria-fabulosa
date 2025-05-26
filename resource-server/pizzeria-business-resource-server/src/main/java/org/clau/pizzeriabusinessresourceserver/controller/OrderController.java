package org.clau.pizzeriabusinessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.validation.ValidationResponses;
import org.clau.pizzeriabusinessassets.validation.order.CompositeValidator;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderProjection;
import org.clau.pizzeriabusinessresourceserver.dao.projection.OrderSummaryProjection;
import org.clau.pizzeriabusinessresourceserver.service.OrderService;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.BASE + Route.V1 + Route.ORDER_BASE)
public class OrderController {

	private final OrderService orderService;

	private final CompositeValidator<OrderValidatorInput> newOrderValidator;

	private final Validator<LocalDateTime> deleteOrderValidator;

	@PostMapping
	public ResponseEntity<?> createUserOrder(
			@RequestBody @Valid NewUserOrderDTO order,
			@RequestParam(name = Route.USER_ID_PARAM) Long userId,
			HttpServletRequest request) {

		Optional<ValidationResult> validate = newOrderValidator.validate(new OrderValidatorInput(order.cart(), order.orderDetails()));

		if (validate.isPresent()) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseDTO.builder()
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
					.build());
		}

		Order createdOrder = orderService.createUserOrder(userId, order);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	}

	@GetMapping(Route.ORDER_ID)
	public ResponseEntity<?> findOrderDTOById(@PathVariable Long orderId) {

		Optional<OrderProjection> order = orderService.findOrderDTOById(orderId);

		return order.map(orderDTO -> ResponseEntity.ok().body(orderDTO)).orElse(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
	}

	@DeleteMapping(Route.ORDER_ID)
	public ResponseEntity<?> deleteOrderById(@PathVariable Long orderId, HttpServletRequest request) {

		Optional<CreatedOnProjection> createdOnDTOById = orderService.findCreatedOnDTOById(orderId);

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
						.build());
			}
		}

		orderService.deleteUserOrderById(orderId);
		return ResponseEntity.ok(orderId);
	}

	@GetMapping(Route.ORDER_SUMMARY)
	public ResponseEntity<?> findUserOrdersSummary(
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize,
			@RequestParam(name = Route.USER_ID_PARAM) Long userId) {

		Page<OrderSummaryProjection> orderSummaryPage = orderService.findUserOrderSummary(userId, pageSize, pageNumber);

		if (orderSummaryPage.getTotalElements() == 0) {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
		}

		return ResponseEntity.ok(orderSummaryPage);
	}
}
