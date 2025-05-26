package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NewAnonOrderDTO(

		@NotNull
		@Valid
		CustomerDTO customer,

		@NotNull
		@Valid
		String address,

		@NotNull
		@Valid
		OrderDetailsDTO orderDetails,

		@NotNull
		@Valid
		CartDTO cart
) {
}