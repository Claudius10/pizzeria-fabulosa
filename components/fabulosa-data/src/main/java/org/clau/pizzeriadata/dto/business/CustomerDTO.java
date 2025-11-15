package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CustomerDTO(

   @NotBlank
   String anonCustomerName,

   @NotNull
   @Positive
   Integer anonCustomerContactNumber,

   @NotBlank
   @Email(message = "InvalidEmail")
   String anonCustomerEmail

) {
}
