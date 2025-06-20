package org.clau.pizzeriabusinessassets.validation.order;

public interface Validator<T> {

   ValidationResult validate(T object);
}
