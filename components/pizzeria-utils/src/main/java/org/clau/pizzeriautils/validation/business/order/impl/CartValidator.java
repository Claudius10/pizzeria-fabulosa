package org.clau.pizzeriautils.validation.business.order.impl;

import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.dto.business.CartDTO;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.clau.pizzeriautils.validation.business.order.Validator;

public class CartValidator implements Validator<NewOrder> {

   public ValidationResult validate(NewOrder order) {

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
