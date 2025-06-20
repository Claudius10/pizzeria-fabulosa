package org.clau.pizzeriapublicassets.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriapublicassets.model.Offer;

import java.util.List;

public record OfferListDTO(
   @NotNull
   List<Offer> offers
) {
}
