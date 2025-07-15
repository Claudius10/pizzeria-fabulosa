package org.clau.pizzeriasecurityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.pizzeriadata.model.common",
   "org.clau.pizzeriadata.model.user"
})
@EnableJpaRepositories(basePackages = {
   "org.clau.pizzeriadata.dao.common",
   "org.clau.pizzeriadata.dao.user"
})
public class PizzeriaSecurity {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaSecurity.class, args);
   }

}
