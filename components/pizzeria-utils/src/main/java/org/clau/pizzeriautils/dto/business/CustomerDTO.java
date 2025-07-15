package org.clau.pizzeriautils.dto.business;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.constant.common.ValidationRules;
import org.clau.pizzeriautils.validation.common.annotation.IntegerLength;

public record CustomerDTO(

   @NotNull
   @Pattern(regexp = ValidationRules.SIMPLE_LETTERS_ONLY_MAX_50_INSENSITIVE_REQUIERED, message = ValidationResponses.NAME_INVALID)
   String anonCustomerName,

   @NotNull
   @IntegerLength(min = 9, max = 9, message = ValidationResponses.NUMBER_INVALID)
   Integer anonCustomerContactNumber,

   @NotNull
   @Email(message = ValidationResponses.EMAIL_INVALID)
   String anonCustomerEmail
) {
}
