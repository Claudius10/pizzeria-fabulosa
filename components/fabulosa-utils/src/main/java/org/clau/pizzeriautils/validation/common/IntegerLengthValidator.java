package org.clau.pizzeriautils.validation.common;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.clau.pizzeriautils.validation.common.annotation.IntegerLength;

public class IntegerLengthValidator implements ConstraintValidator<IntegerLength, Integer> {

   private int min;

   private int max;

   @Override
   public void initialize(IntegerLength integerLength) {
	  this.min = integerLength.min();
	  this.max = integerLength.max();
   }

   @Override
   public boolean isValid(Integer value, ConstraintValidatorContext context) {
	  if (value == null) {
		 return false;
	  }

	  int valueLength = String.valueOf(value.intValue()).length();
	  return (valueLength >= min && valueLength <= max);
   }
}