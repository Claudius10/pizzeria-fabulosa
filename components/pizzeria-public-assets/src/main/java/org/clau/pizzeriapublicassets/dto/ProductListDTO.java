package org.clau.pizzeriapublicassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriapublicassets.model.Product;

import java.util.List;

public record ProductListDTO(
   @NotNull
   List<Product> content,

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