package org.clau.pizzeriabusinessresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.pizzeriadata.model.common",
   "org.clau.pizzeriadata.model.business",
   "org.clau.pizzeriadata.model.user",
})
@EnableJpaRepositories(basePackages = {
   "org.clau.pizzeriadata.dao.common",
   "org.clau.pizzeriadata.dao.business",
   "org.clau.pizzeriadata.dao.user",
})
public class PizzeriaBusinessResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaBusinessResource.class, args);
   }

}
