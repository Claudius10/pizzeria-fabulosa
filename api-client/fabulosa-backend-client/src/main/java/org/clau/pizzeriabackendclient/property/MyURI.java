package org.clau.pizzeriabackendclient.property;

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

   private List<String> allowedOrigins;

   @Override
   public void afterPropertiesSet() {
	  Assert.hasText(angularBase, "angularBase uri is required");
	  Assert.hasText(angularDomain, "angularDomain uri is required");
	  Assert.notEmpty(allowedOrigins, "allowedOrigins is required");
   }
}
