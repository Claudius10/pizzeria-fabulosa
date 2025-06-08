package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.constant.ValidationRules;

public record NewAnonOrderDTO(

		@NotNull
		@Valid
		CustomerDTO customer,

		@Pattern(regexp = ValidationRules.COMPLEX_LETTERS_NUMBERS_MAX_150_REQUIRED, message = ValidationResponses.ADDRESS_INVALID)
		String address,

		@NotNull
		@Valid
		OrderDetailsDTO orderDetails,

		@NotNull
		@Valid
		CartDTO cart
) {
}