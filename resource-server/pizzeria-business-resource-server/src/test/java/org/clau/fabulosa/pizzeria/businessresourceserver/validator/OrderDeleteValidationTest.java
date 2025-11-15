package org.clau.fabulosa.pizzeria.businessresourceserver.validator;

import org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl.OrderCancelTimeLimitValidator;
import org.clau.fabulosa.utils.constant.ValidationResponses;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class OrderDeleteValidationTest {

   @Test
   void givenOrderDeleteRequest_whenDeleteWindowPassed_thenReturnInvalidResult() {
	  // Arrange

	  OrderCancelTimeLimitValidator orderCancelTimeLimitValidator = new OrderCancelTimeLimitValidator();
	  LocalDateTime now = LocalDateTime.now();
	  LocalDateTime date = now.minusMinutes(15);

	  // Act

	  ValidationResult isDeleteRequestValid = orderCancelTimeLimitValidator.validate(date);

	  // Assert

	  assertThat(isDeleteRequestValid.valid()).isFalse();
	  assertThat(isDeleteRequestValid.message()).isEqualTo(ValidationResponses.ORDER_CANCEL_TIME_ERROR);
   }

   @Test
   void givenOrderDeleteRequest_whenDeleteWindowDidNotPass_thenReturnValidResult() {
	  // Arrange

	  OrderCancelTimeLimitValidator orderCancelTimeLimitValidator = new OrderCancelTimeLimitValidator();
	  LocalDateTime now = LocalDateTime.now();
	  LocalDateTime date = now.plusMinutes(15);

	  // Act

	  ValidationResult isDeleteRequestValid = orderCancelTimeLimitValidator.validate(date);

	  // Assert

	  assertThat(isDeleteRequestValid.valid()).isTrue();
	  assertThat(isDeleteRequestValid.message()).isNull();
   }
}