package org.clau.fabulosa.pizzeria.businessresourceserver.validator;

public record ValidationResult(
   String message,
   Boolean valid
) {
}