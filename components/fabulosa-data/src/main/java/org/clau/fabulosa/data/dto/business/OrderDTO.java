package org.clau.fabulosa.data.dto.business;

import jakarta.validation.constraints.NotNull;

/**
 * Return DTO for finding user order by id
 */
public record OrderDTO(

   @NotNull
   Long id,

   @NotNull
   String formattedCreatedOn,

   @NotNull
   String state,

   @NotNull
   String address,

   @NotNull
   OrderDetailsDTO orderDetails,

   @NotNull
   CartDTO cart

) {
}
