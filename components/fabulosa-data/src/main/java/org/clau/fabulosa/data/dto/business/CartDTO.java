package org.clau.fabulosa.data.dto.business;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record CartDTO(

   @NotNull
   Integer totalQuantity,

   @NotNull
   Double totalCost,

   @NotNull
   Double totalCostOffers,

   @NotNull
   List<CartItemDTO> cartItems

) {
}
