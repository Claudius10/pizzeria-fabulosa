package org.clau.pizzeriautils.validation.business.order.impl;

import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.dto.business.CartDTO;
import org.clau.pizzeriautils.dto.business.OrderDetailsDTO;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.clau.pizzeriautils.validation.business.order.Validator;

public class OrderDetailsValidator implements Validator<NewOrder> {

   public ValidationResult validate(NewOrder order) {

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
