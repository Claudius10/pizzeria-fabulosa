package org.clau.pizzeriaassetsclient.dto;

import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriastoreassets.model.Product;

import java.util.List;

public record ProductListDTO(
		@NotNull
		List<Product> productList,

		@NotNull
		int totalPages,

		@NotNull
		int pageSize,

		@NotNull
		long totalElements,

		@NotNull
		boolean hasNext
) {
}