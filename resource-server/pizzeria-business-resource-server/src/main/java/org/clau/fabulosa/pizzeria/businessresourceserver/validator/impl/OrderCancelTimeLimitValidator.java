package org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl;

import org.clau.fabulosa.pizzeria.businessresourceserver.validator.ValidationResult;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.clau.fabulosa.utils.constant.ValidationResponses;

import java.time.LocalDateTime;

public class OrderCancelTimeLimitValidator implements Validator<LocalDateTime> {

   private final static int TIME_LIMIT_MIN = 10;

   @Override
   public ValidationResult validate(LocalDateTime createdOn) {

	  boolean isValid = true;
	  String message = null;

	  if (createdOn.plusMinutes(TIME_LIMIT_MIN).isBefore(LocalDateTime.now())) {
		 isValid = false;
		 message = ValidationResponses.ORDER_CANCEL_TIME_ERROR;
	  }

	  return new ValidationResult(message, isValid);
   }
}