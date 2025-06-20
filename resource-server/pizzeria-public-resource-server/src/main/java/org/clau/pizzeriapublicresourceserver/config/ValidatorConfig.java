package org.clau.pizzeriapublicresourceserver.config;


import org.clau.pizzeriabusinessassets.validation.order.CompositeValidator;
import org.clau.pizzeriabusinessassets.validation.order.OrderValidatorInput;
import org.clau.pizzeriabusinessassets.validation.order.Validator;
import org.clau.pizzeriabusinessassets.validation.order.impl.CartValidator;
import org.clau.pizzeriabusinessassets.validation.order.impl.DeleteTimeLimitValidator;
import org.clau.pizzeriabusinessassets.validation.order.impl.NewOrderValidator;
import org.clau.pizzeriabusinessassets.validation.order.impl.OrderDetailsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ValidatorConfig {

   @Bean
   CompositeValidator<OrderValidatorInput> newOrderValidator() {
	  NewOrderValidator newOrderValidator = new NewOrderValidator();
	  newOrderValidator.setValidators(List.of(new CartValidator(), new OrderDetailsValidator()));
	  return newOrderValidator;
   }

   @Bean
   Validator<LocalDateTime> deleteOrderValidator() {
	  return new DeleteTimeLimitValidator();
   }
}
