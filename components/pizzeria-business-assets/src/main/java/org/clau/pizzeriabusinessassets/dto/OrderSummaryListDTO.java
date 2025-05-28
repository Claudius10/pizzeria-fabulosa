package org.clau.pizzeriabusinessassets.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotNull;
import org.clau.pizzeriabusinessassets.jackson.OrderSummaryListDeserializer;

import java.util.List;

@JsonDeserialize(using = OrderSummaryListDeserializer.class)
public record OrderSummaryListDTO(

		@NotNull
		List<OrderSummaryDTO> content,

		@NotNull
		int number,

		@NotNull
		int size,

		@NotNull
		long totalElements,

		@NotNull
		boolean last
) {
}