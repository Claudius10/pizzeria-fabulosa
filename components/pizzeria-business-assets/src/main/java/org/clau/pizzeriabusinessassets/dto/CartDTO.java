package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.validation.annotation.DoubleLength;
import org.clau.apiutils.validation.annotation.DoubleLengthNullable;
import org.clau.apiutils.validation.annotation.IntegerLength;

import java.util.List;

public record CartDTO(

   @NotNull
   @IntegerLength(min = 1, max = 2, message = ValidationResponses.CART_MAX_PRODUCTS_QUANTITY_ERROR)
   Integer totalQuantity,

   @NotNull
   @DoubleLength(min = 1, max = 6, message = ValidationResponses.TOTAL_COST_ERROR)
   Double totalCost,

   @NotNull
   @DoubleLengthNullable(min = 0, max = 6, message = ValidationResponses.TOTAL_COST_AFTER_OFFERS_ERROR)
   Double totalCostOffers,

   @NotNull
   List<CartItemDTO> cartItems
) {
}
