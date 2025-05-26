package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record OrderDTO(
		@NotNull
		Long id,

		@NotNull
		LocalDateTime createdOn,

		@NotNull
		String formattedCreatedOn,

		@NotNull
		String address,

		@NotNull
		OrderDetailsDTO orderDetails,

		@NotNull
		CartDTO cart
) {
}
