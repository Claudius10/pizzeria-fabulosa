package org.clau.pizzeriasecurityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.pizzeriasecurityserver.data.model",
   "org.clau.pizzeriadata.model.common"
})
public class PizzeriaSecurity {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaSecurity.class, args);
   }

}
