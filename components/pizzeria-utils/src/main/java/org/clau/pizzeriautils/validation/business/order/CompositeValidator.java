package org.clau.pizzeriautils.validation.business.order;

import java.util.Optional;

public interface CompositeValidator<T> {

   Optional<ValidationResult> validate(T object);
}
