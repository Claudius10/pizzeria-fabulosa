package org.clau.fabulosa.pizzeria.publicresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.fabulosa.data.model.common",
   "org.clau.fabulosa.data.model.assets",
   "org.clau.fabulosa.data.model.business"
})
public class PizzeriaPublicResourceServer {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaPublicResourceServer.class, args);
   }
}
