package org.clau.fabulosa.utils.validation.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.clau.fabulosa.utils.validation.common.annotation.DoubleLengthNullable;

public class DoubleLengthNullableValidator implements ConstraintValidator<DoubleLengthNullable, Double> {

   private double min;

   private double max;

   @Override
   public void initialize(DoubleLengthNullable doubleLength) {
	  this.min = doubleLength.min();
	  this.max = doubleLength.max();
   }

   @Override
   public boolean isValid(Double value, ConstraintValidatorContext context) {
	  if (value != null) {
		 int length = String.valueOf(value.doubleValue()).length();
		 return (length >= min && length <= max);
	  } else {
		 return true;
	  }
   }
}