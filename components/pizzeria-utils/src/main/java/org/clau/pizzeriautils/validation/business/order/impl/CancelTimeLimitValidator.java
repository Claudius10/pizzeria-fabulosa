package org.clau.pizzeriautils.validation.business.order.impl;

import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.clau.pizzeriautils.validation.business.order.Validator;

import java.time.LocalDateTime;

public class CancelTimeLimitValidator implements Validator<LocalDateTime> {

   private final static int TIME_LIMIT_MIN = 10;

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