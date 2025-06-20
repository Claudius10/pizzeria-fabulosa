package org.clau.pizzeriapublicresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages =
   {
	  "org.clau.pizzeriapublicassets.model",
	  "org.clau.pizzeriabusinessassets.model",
	  "org.clau.pizzeriauserassets.model",
	  "org.clau.apiutils.model"
   }
)
public class PizzeriaPublicResource {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaPublicResource.class, args);
   }
}
