package org.clau.pizzeriabusinessresourceserver.validator;

import java.util.Optional;

public interface CompositeValidator<T> {

   Optional<ValidationResult> validate(T object);
}
