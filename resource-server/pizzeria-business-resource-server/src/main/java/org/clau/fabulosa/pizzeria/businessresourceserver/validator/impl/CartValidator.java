package org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl;

import org.clau.fabulosa.pizzeria.businessresourceserver.validator.OrderToValidate;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.ValidationResult;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.clau.fabulosa.data.dto.business.CartDTO;
import org.clau.fabulosa.utils.constant.ValidationResponses;

public class CartValidator implements Validator<OrderToValidate> {

   @Override
   public ValidationResult validate(OrderToValidate order) {

	  boolean isValid = true;
	  String message = null;

	  if (isCartEmpty(order.cart())) {
		 isValid = false;
		 message = ValidationResponses.CART_IS_EMPTY;
	  }

	  return new ValidationResult(message, isValid);
   }

   private boolean isCartEmpty(CartDTO cart) {
	  return cart == null || cart.cartItems().isEmpty() || cart.totalQuantity() == 0;
   }
}
