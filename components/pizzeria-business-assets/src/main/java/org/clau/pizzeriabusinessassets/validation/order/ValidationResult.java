package org.clau.pizzeriabusinessassets.validation.order;

public record ValidationResult(
		String message,
		Boolean valid
) {
}