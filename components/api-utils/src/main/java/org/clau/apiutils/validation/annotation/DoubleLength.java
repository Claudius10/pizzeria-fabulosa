package org.clau.apiutils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.clau.apiutils.validation.DoubleLengthValidator;

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