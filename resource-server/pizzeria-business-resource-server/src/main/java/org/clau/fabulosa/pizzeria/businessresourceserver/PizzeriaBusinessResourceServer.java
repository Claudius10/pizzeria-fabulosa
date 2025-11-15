package org.clau.fabulosa.pizzeria.businessresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.fabulosa.data.model.common",
   "org.clau.fabulosa.data.model.business",
})
public class PizzeriaBusinessResourceServer {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaBusinessResourceServer.class, args);
   }

}
