package org.clau.pizzeriabusinessresourceserver.validator;

public interface Validator<T> {

   ValidationResult validate(T object);
}
