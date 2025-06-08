package org.clau.apiutils.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.clau.apiutils.validation.IntegerLengthValidator;

import java.lang.annotation.*;

/**
 * Null values are not considered valid.
 *
 * @author Claudiu Catalin
 */

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = IntegerLengthValidator.class)
public @interface IntegerLength {
	String message() default "InvalidIntegerValue";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

	int min();

	int max();
}
