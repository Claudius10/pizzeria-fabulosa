package org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl;

import org.clau.fabulosa.pizzeria.businessresourceserver.validator.OrderToValidate;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.ValidationResult;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.clau.fabulosa.data.dto.business.CartDTO;
import org.clau.fabulosa.data.dto.business.OrderDetailsDTO;
import org.clau.fabulosa.utils.constant.ValidationResponses;

public class OrderDetailsValidator implements Validator<OrderToValidate> {

   @Override
   public ValidationResult validate(OrderToValidate order) {

	  CartDTO cart = order.cart();
	  OrderDetailsDTO orderDetails = order.orderDetails();
	  Double billToChange = orderDetails.billToChange() == null ? 0 : orderDetails.billToChange();
	  Double totalCost = cart.totalCost();
	  Double totalCostOffers = cart.totalCostOffers() == null ? 0 : cart.totalCostOffers();

	  boolean isValid = true;
	  String message = null;

	  if (!isChangeRequestedValid(billToChange, totalCostOffers, totalCost)) {
		 isValid = false;
		 message = ValidationResponses.ORDER_DETAILS_BILL;
	  }

	  return new ValidationResult(message, isValid);
   }

   // changeRequested > totalCostAfterOffers || changeRequested > totalCost
   private boolean isChangeRequestedValid(Double billToChange, Double totalCostAfterOffers, Double totalCost) {
	  if (billToChange == null || billToChange == 0) {
		 return true;
	  }
	  return (totalCostAfterOffers <= 0 || billToChange >= totalCostAfterOffers) && (totalCostAfterOffers != 0 || billToChange >= totalCost);
   }
}
