package org.clau.fabulosa.pizzeria.businessresourceserver.validator;

import java.util.Optional;

public interface CompositeValidator<T> {

   Optional<ValidationResult> validate(T object);
}
