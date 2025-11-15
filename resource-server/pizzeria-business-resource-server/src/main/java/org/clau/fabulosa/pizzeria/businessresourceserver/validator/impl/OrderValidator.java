package org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.CompositeValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.OrderToValidate;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.ValidationResult;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Setter
@RequiredArgsConstructor
public class OrderValidator implements CompositeValidator<OrderToValidate>, InitializingBean {

   private List<Validator<OrderToValidate>> validators;

   @Override
   public Optional<ValidationResult> validate(OrderToValidate order) {

	  for (Validator<OrderToValidate> validator : validators) {
		 ValidationResult result = validator.validate(order);

		 if (!result.valid()) {
			return Optional.of(result);
		 }
	  }

	  return Optional.empty();
   }

   @Override
   public void afterPropertiesSet() {
	  Assert.notNull(validators, "validators is required");
   }
}
