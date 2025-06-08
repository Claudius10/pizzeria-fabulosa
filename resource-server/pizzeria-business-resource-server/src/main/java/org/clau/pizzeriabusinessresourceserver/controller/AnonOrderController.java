package org.clau.pizzeriabusinessresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessassets.dto.NewAnonOrderDTO;
import org.clau.pizzeriabusinessassets.model.Order;
import org.clau.pizzeriabusinessassets.validation.order.CompositeValidator;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessresourceserver.service.AnonOrderService;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
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
public class AnonOrderController {

	private final AnonOrderService orderService;

	private final CompositeValidator<OrderValidatorInput> newOrderValidator;

	@PostMapping
	public ResponseEntity<?> createAnonOrder(@RequestBody @Valid NewAnonOrderDTO newAnonOrder, HttpServletRequest request) {

		Optional<ValidationResult> validate = newOrderValidator.validate(new OrderValidatorInput(newAnonOrder.cart(), newAnonOrder.orderDetails()));

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

		Order createdOrder = orderService.createAnonOrder(newAnonOrder);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
	}
}
