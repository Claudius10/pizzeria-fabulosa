package org.clau.pizzeriabusinessassets.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.clau.apiutils.constant.ValidationResponses;
import org.clau.apiutils.constant.ValidationRules;

public record OrderDetailsDTO(

		@NotBlank(message = ValidationResponses.ORDER_DETAILS_DELIVERY_HOUR)
		String deliveryTime,

		@NotBlank(message = ValidationResponses.ORDER_DETAILS_PAYMENT)
		String paymentMethod,

		Double billToChange,

		@Pattern(regexp = ValidationRules.COMPLEX_LETTERS_NUMBERS_MAX_150_OPTIONAL, message = ValidationResponses.ORDER_DETAILS_COMMENT)
		String comment,

		@NotNull
		Boolean storePickUp,

		Double changeToGive
) {
}
