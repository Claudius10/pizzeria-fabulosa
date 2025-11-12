package org.clau.pizzeriabusinessresourceserver.config;

import org.clau.pizzeriabusinessresourceserver.validator.CompositeValidator;
import org.clau.pizzeriabusinessresourceserver.validator.OrderToValidate;
import org.clau.pizzeriabusinessresourceserver.validator.Validator;
import org.clau.pizzeriabusinessresourceserver.validator.impl.CartValidator;
import org.clau.pizzeriabusinessresourceserver.validator.impl.OrderCancelTimeLimitValidator;
import org.clau.pizzeriabusinessresourceserver.validator.impl.OrderDetailsValidator;
import org.clau.pizzeriabusinessresourceserver.validator.impl.OrderValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ValidatorConfig {

   @Bean
   CompositeValidator<OrderToValidate> newOrderValidator() {
	  OrderValidator orderValidator = new OrderValidator();
	  orderValidator.setValidators(List.of(new CartValidator(), new OrderDetailsValidator()));
	  return orderValidator;
   }

   @Bean
   Validator<LocalDateTime> cancelOrderValidator() {
	  return new OrderCancelTimeLimitValidator();
   }
}
