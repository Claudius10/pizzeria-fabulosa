package org.clau.pizzeriaadminresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.pizzeriadata.model",
})
@EnableJpaRepositories(basePackages = {
   "org.clau.pizzeriadata.dao.admin",
})
public class PizzeriaAdminResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaAdminResource.class, args);
   }

}
