package org.clau.fabulosa.data.dto.assets;

import jakarta.validation.constraints.NotNull;
import org.clau.fabulosa.data.model.assets.Offer;

import java.util.List;

public record OfferListDTO(

   @NotNull
   List<Offer> offers

) {
}
