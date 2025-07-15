package org.clau.pizzeriapublicresourceserver.config;

import org.clau.pizzeriautils.validation.business.order.CompositeValidator;
import org.clau.pizzeriautils.validation.business.order.NewOrder;
import org.clau.pizzeriautils.validation.business.order.Validator;
import org.clau.pizzeriautils.validation.business.order.impl.CartValidator;
import org.clau.pizzeriautils.validation.business.order.impl.DeleteTimeLimitValidator;
import org.clau.pizzeriautils.validation.business.order.impl.NewOrderValidator;
import org.clau.pizzeriautils.validation.business.order.impl.OrderDetailsValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;
import java.util.List;

@Configuration(proxyBeanMethods = false)
public class ValidatorConfig {

   @Bean
   CompositeValidator<NewOrder> newOrderValidator() {
	  NewOrderValidator newOrderValidator = new NewOrderValidator();
	  newOrderValidator.setValidators(List.of(new CartValidator(), new OrderDetailsValidator()));
	  return newOrderValidator;
   }

   @Bean
   Validator<LocalDateTime> deleteOrderValidator() {
	  return new DeleteTimeLimitValidator();
   }
}
