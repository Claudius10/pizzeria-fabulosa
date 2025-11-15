package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;

public record CartItemDTO(

   Long id,

   @NotBlank
   String type,

   @NotNull
   @Positive
   Double price,

   @NotNull
   @Positive
   Integer quantity,

   @NotEmpty
   Map<String, String> name,

   @NotEmpty
   Map<String, List<String>> description,

   @NotEmpty
   Map<String, Map<String, String>> formats

) {
}
