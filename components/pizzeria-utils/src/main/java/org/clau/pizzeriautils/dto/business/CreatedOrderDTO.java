package org.clau.pizzeriautils.dto.business;

import jakarta.validation.constraints.NotNull;

public record CreatedOrderDTO(

   @NotNull
   Long id,

   @NotNull
   String formattedCreatedOn,

   CustomerDTO customer,

   @NotNull
   String address,

   @NotNull
   OrderDetailsDTO orderDetails,

   @NotNull
   CartDTO cart) {
}