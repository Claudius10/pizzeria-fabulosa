package org.clau.pizzeriauserresourceserver.config;

import org.clau.pizzeriadata.dao.common.ErrorRepository;
import org.clau.pizzeriadata.service.common.ErrorService;
import org.clau.pizzeriadata.service.common.impl.ErrorServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MyBeansConfig {

   @Bean
   ErrorService errorService(ErrorRepository errorRepository) {
	  return new ErrorServiceImpl(errorRepository);
   }
}
