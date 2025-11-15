package org.clau.pizzeriadata.dto.assets;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriadata.model.assets.Offer;

import java.util.List;

public record OfferListDTO(

   @NotNull
   List<Offer> offers

) {
}
