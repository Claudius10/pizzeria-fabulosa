package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.NotNull;

public record OrderSummaryDTO(

   @NotNull
   Long id,

   @NotNull
   String formattedCreatedOn,

   @NotNull
   String state,

   @NotNull
   String paymentMethod,

   @NotNull
   Integer quantity,

   @NotNull
   Double cost,

   @NotNull
   Double costAfterOffers

) {
}
