package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OrderDetailsDTO(

   @NotBlank(message = "InvalidOrderDetailsDeliveryHour")
   String deliveryTime,

   @NotBlank(message = "InvalidOrderDetailsPayment")
   String paymentMethod,

   Double billToChange,

   String comment,

   @NotNull
   Boolean storePickUp,

   Double changeToGive

) {
}
