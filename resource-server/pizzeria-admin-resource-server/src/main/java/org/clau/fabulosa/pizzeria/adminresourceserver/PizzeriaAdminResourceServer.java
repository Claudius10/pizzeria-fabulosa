package org.clau.fabulosa.pizzeria.adminresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.fabulosa.data.model"})
public class PizzeriaAdminResourceServer {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaAdminResourceServer.class, args);
   }

}
