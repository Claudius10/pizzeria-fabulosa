package org.clau.fabulosa.data.dto.assets;

import jakarta.validation.constraints.NotNull;
import org.clau.fabulosa.data.model.assets.Store;

import java.util.List;

public record StoreListDTO(

   @NotNull
   List<Store> stores

) {
}
