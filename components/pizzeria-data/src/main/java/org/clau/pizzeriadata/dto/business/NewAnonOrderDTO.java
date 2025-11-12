package org.clau.pizzeriadata.dto.business;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record NewAnonOrderDTO(

   @NotNull
   @Valid
   CustomerDTO customer,

   @NotBlank
   String address,

   @NotNull
   @Valid
   OrderDetailsDTO orderDetails,

   @NotNull
   @Valid
   CartDTO cart

) {
}