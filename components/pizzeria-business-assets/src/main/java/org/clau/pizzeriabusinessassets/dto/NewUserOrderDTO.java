package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record NewUserOrderDTO(

		@NotNull
		String address,

		@NotNull
		@Valid
		OrderDetailsDTO orderDetails,

		@NotNull
		@Valid
		CartDTO cart) {
}