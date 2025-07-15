package org.clau.pizzeriautils.dto.assets;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriadata.model.assets.Product;

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