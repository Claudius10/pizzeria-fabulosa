package org.clau.pizzeriaassetsclient.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriastoreassets.model.Offer;

import java.util.List;

public record OfferListDTO(
		@NotNull
		List<Offer> offers
) {
}
