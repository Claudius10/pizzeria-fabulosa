package org.clau.pizzeriadata.dto.business;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderSummaryListDTO(

   @NotNull
   List<OrderSummaryDTO> content,

   @NotNull
   int number,

   @NotNull
   int size,

   @NotNull
   long totalElements,

   @NotNull
   boolean last

) {
}