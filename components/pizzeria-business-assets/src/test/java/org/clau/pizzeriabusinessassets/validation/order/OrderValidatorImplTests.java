package org.clau.pizzeriabusinessassets.validation.order;

import org.clau.apiutils.constant.ValidationResponses;
import org.clau.pizzeriabusinessassets.dto.CartDTO;
import org.clau.pizzeriabusinessassets.dto.CartItemDTO;
import org.clau.pizzeriabusinessassets.dto.OrderDetailsDTO;
import org.clau.pizzeriabusinessassets.validation.order.impl.CartValidator;
import org.clau.pizzeriabusinessassets.validation.order.impl.OrderDetailsValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OrderValidatorImplTests {

   @Test
   void givenCartValidator_whenEmptyCart_thenReturnInvalidAndMessage() {

	  // Arrange

	  CartValidator cartValidator = new CartValidator();
	  CartDTO cart = new CartDTO(
		 0,
		 0D,
		 0D,
		 List.of()
	  );

	  // Act

	  ValidationResult validate = cartValidator.validate(new OrderValidatorInput(cart, null));

	  // Assert

	  assertThat(validate.valid()).isFalse();
	  assertThat(validate.message()).isEqualTo(ValidationResponses.CART_IS_EMPTY);
   }

   @Test
   void givenCartValidator_whenNonEmptyCart_thenReturnValidWithNullMessage() {

	  // Arrange

	  CartValidator cartValidator = new CartValidator();

	  CartDTO cart = new CartDTO(
		 1,
		 5D,
		 0D,
		 List.of(new CartItemDTO(null, "pizza", 5D, 1, null, null, null)));

	  // Act

	  ValidationResult validate = cartValidator.validate(new OrderValidatorInput(cart, null));

	  // Assert

	  assertThat(validate.valid()).isTrue();
	  assertThat(validate.message()).isNull();
   }

   @Test
   void givenOrderDetailsValidator_whenRequestedChangeIsNotValid_thenReturnInvalidWithMessage() {

	  // Arrange

	  OrderDetailsValidator orderDetailsValidator = new OrderDetailsValidator();


	  CartDTO cart = new CartDTO(
		 1,
		 50D,
		 0D,
		 List.of(new CartItemDTO(null, "pizza", 50D, 1, null, null, null)));

	  Double billToChange = 10D;
	  OrderDetailsDTO orderDetails = new OrderDetailsDTO("ASAP", "Cash", billToChange, null, false, 0D);

	  // Act

	  ValidationResult validate = orderDetailsValidator.validate(new OrderValidatorInput(cart, orderDetails));

	  // Assert

	  assertThat(validate.valid()).isFalse();
	  assertThat(validate.message()).isEqualTo(ValidationResponses.ORDER_DETAILS_BILL);
   }

   @Test
   void givenIsChangeRequestedValidMethod_whenRequestedChangeIsValid_thenReturnTrue() {

	  // Arrange

	  OrderDetailsValidator orderDetailsValidator = new OrderDetailsValidator();

	  CartDTO cart = new CartDTO(
		 1,
		 50D,
		 0D,
		 List.of(new CartItemDTO(null, "pizza", 50D, 1, null, null, null)));

	  Double billToChange = 100D;
	  OrderDetailsDTO orderDetails = new OrderDetailsDTO("ASAP", "Cash", billToChange, null, false, 0D);

	  // Act

	  ValidationResult validate = orderDetailsValidator.validate(new OrderValidatorInput(cart, orderDetails));

	  // Assert

	  assertThat(validate.valid()).isTrue();
	  assertThat(validate.message()).isNull();
   }
}