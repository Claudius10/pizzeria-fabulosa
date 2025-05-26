package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.constraints.NotNull;

public record CreatedOrderDTO(

		@NotNull
		Long id,

		@NotNull
		String formattedCreatedOn,

		@NotNull
		CustomerDTO customer,

		@NotNull
		String address,

		@NotNull
		OrderDetailsDTO orderDetails,

		@NotNull
		CartDTO cart) {
}