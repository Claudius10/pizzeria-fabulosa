package org.clau.pizzeriabusinessassets.validation.order;

import java.util.Optional;

public interface CompositeValidator<T> {

   Optional<ValidationResult> validate(T object);
}
