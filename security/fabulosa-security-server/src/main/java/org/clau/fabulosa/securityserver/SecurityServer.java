package org.clau.fabulosa.securityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
   "org.clau.fabulosa.securityserver.data.model",
   "org.clau.fabulosa.data.model.common"
})
public class SecurityServer {

   public static void main(String[] args) {
	  SpringApplication.run(SecurityServer.class, args);
   }

}
