package org.clau.pizzeriautils.validation.common.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.clau.pizzeriautils.validation.common.FieldMatchValidator;

import java.lang.annotation.*;

@Constraint(validatedBy = FieldMatchValidator.class)
@Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldMatch {
   String message() default "";

   Class<?>[] groups() default {};

   Class<? extends Payload>[] payload() default {};

   String first();

   String second();

   @Target({ElementType.TYPE, ElementType.ANNOTATION_TYPE})
   @Retention(RetentionPolicy.RUNTIME)
   @Documented
   @interface List {
	  FieldMatch[] value();
   }
}