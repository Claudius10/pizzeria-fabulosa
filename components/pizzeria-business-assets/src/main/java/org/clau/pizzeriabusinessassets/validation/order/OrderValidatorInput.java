package org.clau.pizzeriabusinessassets.validation.order;


import org.clau.pizzeriabusinessassets.dto.CartDTO;
import org.clau.pizzeriabusinessassets.dto.OrderDetailsDTO;

public record OrderValidatorInput(
		CartDTO cart,
		OrderDetailsDTO orderDetails
) {
}
