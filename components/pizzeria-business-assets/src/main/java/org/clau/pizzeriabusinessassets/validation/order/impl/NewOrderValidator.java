package org.clau.pizzeriabusinessassets.validation.order.impl;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.clau.pizzeriabusinessassets.validation.order.CompositeValidator;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.ValidationResult;
import org.clau.pizzeriabusinessassets.validation.order.Validator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Setter
@RequiredArgsConstructor
public class NewOrderValidator implements CompositeValidator<OrderValidatorInput>, InitializingBean {

	private List<Validator<OrderValidatorInput>> validators;

	@Override
	public Optional<ValidationResult> validate(OrderValidatorInput order) {

		for (Validator<OrderValidatorInput> validator : validators) {
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
