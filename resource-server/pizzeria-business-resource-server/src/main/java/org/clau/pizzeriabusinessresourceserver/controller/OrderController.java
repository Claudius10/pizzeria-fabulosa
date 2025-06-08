package org.clau.pizzeriabusinessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessassets.dto.NewUserOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.validation.order.CompositeValidator;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;
import org.clau.pizzeriabusinessresourceserver.dao.projection.CreatedOnProjection;
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
@RequestMapping(Route.API + Route.V1 + Route.ORDER_BASE)
public class OrderController {

	private final OrderService orderService;

	private final CompositeValidator<OrderValidatorInput> newOrderValidator;

	private final Validator<LocalDateTime> deleteOrderValidator;

	@PostMapping
	public ResponseEntity<?> create(
			@RequestBody @Valid NewUserOrderDTO order,
			@RequestParam(name = Route.USER_ID_PARAM) Long userId,
			HttpServletRequest request) {

		Optional<ValidationResult> validate = newOrderValidator.validate(new OrderValidatorInput(order.cart(), order.orderDetails()));

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
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	}

	@GetMapping(Route.ORDER_ID)
	public ResponseEntity<?> findById(@PathVariable Long orderId) {

		Optional<Order> order = orderService.findById(orderId);

		return order.map(orderDTO -> ResponseEntity.ok().body(orderDTO))
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
	public ResponseEntity<Page<Order>> findSummary(
			@RequestParam(name = Route.PAGE_NUMBER) Integer pageNumber,
			@RequestParam(name = Route.PAGE_SIZE) Integer pageSize,
			@RequestParam(name = Route.USER_ID_PARAM) Long userId) {

		Page<Order> summary = orderService.findSummary(userId, pageSize, pageNumber);

		return ResponseEntity.ok(summary);
	}
}
