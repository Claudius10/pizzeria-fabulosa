package org.clau.pizzeriautils.validation.business.order;

import org.clau.pizzeriautils.constant.common.ValidationResponses;
import org.clau.pizzeriautils.validation.business.order.impl.CancelTimeLimitValidator;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDeleteValidationTest {

   @Test
   void givenOrderDeleteRequest_whenDeleteWindowPassed_thenReturnInvalidResult() {
	  // Arrange

	  CancelTimeLimitValidator cancelTimeLimitValidator = new CancelTimeLimitValidator();
	  LocalDateTime now = LocalDateTime.now();
	  LocalDateTime date = now.minusMinutes(15);

	  // Act

	  ValidationResult isDeleteRequestValid = cancelTimeLimitValidator.validate(date);

	  // Assert

	  assertThat(isDeleteRequestValid.valid()).isFalse();
	  assertThat(isDeleteRequestValid.message()).isEqualTo(ValidationResponses.ORDER_CANCEL_TIME_ERROR);
   }

   @Test
   void givenOrderDeleteRequest_whenDeleteWindowDidNotPass_thenReturnValidResult() {
	  // Arrange

	  CancelTimeLimitValidator cancelTimeLimitValidator = new CancelTimeLimitValidator();
	  LocalDateTime now = LocalDateTime.now();
	  LocalDateTime date = now.plusMinutes(15);

	  // Act

	  ValidationResult isDeleteRequestValid = cancelTimeLimitValidator.validate(date);

	  // Assert

	  assertThat(isDeleteRequestValid.valid()).isTrue();
	  assertThat(isDeleteRequestValid.message()).isNull();
   }
}