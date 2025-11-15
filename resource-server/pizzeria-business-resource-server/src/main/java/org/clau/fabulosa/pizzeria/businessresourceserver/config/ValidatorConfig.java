package org.clau.fabulosa.pizzeria.businessresourceserver.config;

import org.clau.fabulosa.pizzeria.businessresourceserver.validator.CompositeValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.OrderToValidate;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.Validator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl.CartValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl.OrderCancelTimeLimitValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl.OrderDetailsValidator;
import org.clau.fabulosa.pizzeria.businessresourceserver.validator.impl.OrderValidator;
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
