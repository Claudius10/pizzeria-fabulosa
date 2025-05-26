package org.clau.pizzeriabusinessassets.validation.order.impl;

import org.clau.pizzeriabusinessassets.validation.ValidationResponses;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;

import java.time.LocalDateTime;

public class DeleteTimeLimitValidator implements Validator<LocalDateTime> {

	private final static int UPDATE_TIME_LIMIT_MIN = 10;

	public ValidationResult validate(LocalDateTime createdOn) {

		LocalDateTime limit = createdOn.plusMinutes(UPDATE_TIME_LIMIT_MIN);

		if (limit.isBefore(LocalDateTime.now())) {
			return new ValidationResult(ValidationResponses.ORDER_DELETE_TIME_ERROR, false);
		}

		return new ValidationResult(null, true);
	}
}