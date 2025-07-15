package org.clau.pizzeriautils.validation.business.order;

public interface Validator<T> {

   ValidationResult validate(T object);
}
