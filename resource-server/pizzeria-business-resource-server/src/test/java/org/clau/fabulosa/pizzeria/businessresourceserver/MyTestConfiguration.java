package org.clau.fabulosa.pizzeria.businessresourceserver;

import org.clau.fabulosa.utils.util.JWTKeys;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class MyTestConfiguration {

   @Bean
   @ServiceConnection
   public MariaDBContainer mariadbContainer() {
	  return new MariaDBContainer<>(DockerImageName.parse("mariadb:latest"));
   }

   @Bean
   JWTKeys jwtKeys() {
	  return new JWTKeys();
   }

   @Bean
   JwtDecoder jwtDecoder(JWTKeys keys) {
	  return NimbusJwtDecoder.withPublicKey(keys.getPublicKey()).build();
   }
}
