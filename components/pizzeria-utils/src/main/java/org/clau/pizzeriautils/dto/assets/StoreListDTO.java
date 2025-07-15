package org.clau.pizzeriautils.dto.assets;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriadata.model.assets.Store;

import java.util.List;

public record StoreListDTO(
   @NotNull
   List<Store> stores
) {
}
