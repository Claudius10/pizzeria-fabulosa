package org.clau.pizzeriapublicassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriapublicassets.model.Store;

import java.util.List;

public record StoreListDTO(
   @NotNull
   List<Store> stores
) {
}
