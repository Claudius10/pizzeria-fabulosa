package org.clau.pizzeriastoreassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriastoreassets.model.Store;

import java.util.List;

public record StoreListDTO(
   @NotNull
   List<Store> stores
) {
}
