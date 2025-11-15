package org.clau.fabulosa.backendclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class BackendClient {

   public static void main(String[] args) {
	  SpringApplication.run(BackendClient.class, args);
   }

}
