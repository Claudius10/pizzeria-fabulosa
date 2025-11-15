package org.clau.pizzeriabusinessresourceserver.validator.impl;

import org.clau.pizzeriabusinessresourceserver.validator.OrderToValidate;
import org.clau.pizzeriabusinessresourceserver.validator.ValidationResult;
import org.clau.pizzeriabusinessresourceserver.validator.Validator;
import org.clau.pizzeriadata.dto.business.CartDTO;
import org.clau.pizzeriautils.constant.ValidationResponses;

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
