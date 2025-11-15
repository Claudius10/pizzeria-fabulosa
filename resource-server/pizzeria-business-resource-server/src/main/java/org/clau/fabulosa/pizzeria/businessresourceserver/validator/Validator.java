package org.clau.fabulosa.pizzeria.businessresourceserver.validator;

public interface Validator<T> {

   ValidationResult validate(T object);
}
