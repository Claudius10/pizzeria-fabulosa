package org.clau.pizzeriauserresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.pizzeriauserassets.model", "org.clau.apiutils.model"})
public class PizzeriaUserResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaUserResource.class, args);
   }

}
