package org.clau.pizzeriabusinessassets.validation.order.impl;

import org.clau.pizzeriabusinessassets.dto.CartDTO;
import org.clau.pizzeriabusinessassets.dto.OrderDetailsDTO;
import org.clau.pizzeriabusinessassets.validation.ValidationResponses;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;

public class OrderDetailsValidator implements Validator<OrderValidatorInput> {

	public ValidationResult validate(OrderValidatorInput order) {

		CartDTO cart = order.cart();
		OrderDetailsDTO orderDetails = order.orderDetails();
		Double billToChange = orderDetails.billToChange() == null ? 0 : orderDetails.billToChange();
		Double totalCost = cart.totalCost();
		Double totalCostOffers = cart.totalCostOffers() == null ? 0 : cart.totalCostOffers();

		if (!isChangeRequestedValid(billToChange, totalCostOffers, totalCost)) {
			return new ValidationResult(ValidationResponses.ORDER_DETAILS_BILL, false);
		}

		return new ValidationResult(null, true);
	}

	// changeRequested > totalCostAfterOffers || changeRequested > totalCost
	private boolean isChangeRequestedValid(Double billToChange, Double totalCostAfterOffers, Double totalCost) {
		if (billToChange == null || billToChange == 0) {
			return true;
		}
		return (totalCostAfterOffers <= 0 || billToChange >= totalCostAfterOffers) && (totalCostAfterOffers != 0 || billToChange >= totalCost);
	}
}
