package org.clau.pizzeriabusinessassets.validation.order.impl;

import org.clau.apiutils.constant.ValidationResponses;
import org.clau.pizzeriabusinessassets.dto.CartDTO;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;

public class CartValidator implements Validator<OrderValidatorInput> {

	public ValidationResult validate(OrderValidatorInput order) {

		if (isCartEmpty(order.cart())) {
			return new ValidationResult(ValidationResponses.CART_IS_EMPTY, false);
		}

		return new ValidationResult(null, true);
	}

	private boolean isCartEmpty(CartDTO cart) {
		return cart == null || cart.cartItems().isEmpty() || cart.totalQuantity() == 0;
	}
}
