package org.clau.fabulosa.securityserver.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "uri")
@Setter
@Getter
public class MyURI implements InitializingBean {

   private String angularBase;

   private String angularDomain;

   private String pizzeriaApiBase;

   private List<String> allowedOrigins;

   @Override
   public void afterPropertiesSet() {
	  Assert.hasText(angularBase, "angularBase is required");
	  Assert.hasText(angularDomain, "angularDomain is required");
	  Assert.notEmpty(allowedOrigins, "allowedOrigins is required");
	  Assert.hasText(pizzeriaApiBase, "pizzeriaApiBase is required");
   }
}


