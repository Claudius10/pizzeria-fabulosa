package org.clau.pizzeriabusinessresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.pizzeriabusinessassets.model",
   "org.clau.apiutils.model"
})
public class PizzeriaBusinessResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaBusinessResource.class, args);
   }

}
