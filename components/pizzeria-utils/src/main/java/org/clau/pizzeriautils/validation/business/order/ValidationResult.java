package org.clau.pizzeriautils.validation.business.order;

public record ValidationResult(
   String message,
   Boolean valid
) {
}