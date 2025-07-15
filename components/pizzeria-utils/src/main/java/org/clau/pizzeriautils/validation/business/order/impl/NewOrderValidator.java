package org.clau.pizzeriautils.validation.business.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.clau.pizzeriautils.validation.business.order.CompositeValidator;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.ValidationResult;
import org.clau.pizzeriautils.validation.business.order.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Setter
@RequiredArgsConstructor
public class NewOrderValidator implements CompositeValidator<NewOrder>, InitializingBean {

   private List<Validator<NewOrder>> validators;

   @Override
   public Optional<ValidationResult> validate(NewOrder order) {

	  for (Validator<NewOrder> validator : validators) {
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
