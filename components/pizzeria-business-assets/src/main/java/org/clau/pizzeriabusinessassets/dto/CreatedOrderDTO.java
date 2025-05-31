package org.clau.pizzeriabusinessassets.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriabusinessassets.jackson.OrderDeserializer;

/**
 * Return DTO for newly created orders, anonymously or by user.
 */
@JsonDeserialize(using = OrderDeserializer.class)
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