package org.clau.pizzeriaadminresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.pizzeriadata.model"})
public class PizzeriaAdminResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaAdminResource.class, args);
   }

}
