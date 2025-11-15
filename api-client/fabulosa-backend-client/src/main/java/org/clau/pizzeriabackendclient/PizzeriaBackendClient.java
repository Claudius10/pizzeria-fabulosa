package org.clau.pizzeriabackendclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PizzeriaBackendClient {

   public static void main(String[] args) {
	  SpringApplication.run(PizzeriaBackendClient.class, args);
   }

}
