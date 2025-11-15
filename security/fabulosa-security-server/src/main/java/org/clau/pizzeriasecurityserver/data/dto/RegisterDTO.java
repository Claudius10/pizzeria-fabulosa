package org.clau.pizzeriasecurityserver.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.clau.pizzeriautils.constant.ValidationResponses;
import org.clau.pizzeriautils.validation.common.annotation.FieldMatch;

@FieldMatch.List(
   {
	  @FieldMatch(first = "email", second = "matchingEmail", message = ValidationResponses.EMAIL_NO_MATCH),
	  @FieldMatch(first = "password", second = "matchingPassword", message = ValidationResponses.PASSWORD_NO_MATCH)
   }
)
public record RegisterDTO(

   @NotBlank
   String name,

   @NotBlank
   @Email
   String email,

   @NotBlank
   @Email
   String matchingEmail,

   @NotNull
   @Positive
   Integer contactNumber,

   @NotBlank
   String password,

   @NotBlank
   String matchingPassword

) {
}