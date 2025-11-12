package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.NotNull;

public record CreatedOrderDTO(

   @NotNull
   Long id,

   @NotNull
   String formattedCreatedOn,

   @NotNull
   String state,

   CustomerDTO customer,

   @NotNull
   String address,

   @NotNull
   OrderDetailsDTO orderDetails,

   @NotNull
   CartDTO cart

) {
}