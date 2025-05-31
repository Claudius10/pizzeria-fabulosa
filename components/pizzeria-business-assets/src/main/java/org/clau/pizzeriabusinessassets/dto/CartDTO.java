package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriabusinessassets.validation.constraints.annotation.DoubleLength;
import org.clau.pizzeriabusinessassets.validation.constraints.annotation.DoubleLengthNullable;
import org.clau.pizzeriabusinessassets.validation.constraints.annotation.IntegerLength;

import java.util.List;

import static org.clau.pizzeriabusinessassets.validation.ValidationResponses.*;

public record CartDTO(

		@NotNull
		@IntegerLength(min = 1, max = 2, message = CART_MAX_PRODUCTS_QUANTITY_ERROR)
		Integer totalQuantity,

		@NotNull
		@DoubleLength(min = 1, max = 6, message = TOTAL_COST_ERROR)
		Double totalCost,

		@NotNull
		@DoubleLengthNullable(min = 0, max = 6, message = TOTAL_COST_AFTER_OFFERS_ERROR)
		Double totalCostOffers,

		@NotNull
		List<CartItemDTO> cartItems
) {
}
