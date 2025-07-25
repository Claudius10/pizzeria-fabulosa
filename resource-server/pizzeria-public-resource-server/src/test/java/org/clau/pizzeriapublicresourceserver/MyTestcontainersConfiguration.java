package org.clau.pizzeriapublicresourceserver;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class MyTestcontainersConfiguration {

   @Bean
   @ServiceConnection
   public MariaDBContainer mariadbContainer() {
	  return new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"));
   }
}
