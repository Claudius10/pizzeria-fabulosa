package org.clau.pizzeriabusinessresourceserver.validator;

public record ValidationResult(
   String message,
   Boolean valid
) {
}