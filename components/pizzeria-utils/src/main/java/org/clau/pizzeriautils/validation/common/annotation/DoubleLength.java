package org.clau.pizzeriautils.validation.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.clau.pizzeriautils.validation.common.DoubleLengthValidator;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = DoubleLengthValidator.class)
@Documented
public @interface DoubleLength {

   String message() default "Valor numérico no aceptado";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   double min();

   double max();
}