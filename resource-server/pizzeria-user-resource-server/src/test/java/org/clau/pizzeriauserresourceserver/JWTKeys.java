package org.clau.pizzeriauserresourceserver;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.interfaces.RSAPublicKey;

@Component
@RequiredArgsConstructor
public class JWTKeys {

   private KeyPair keyPair;

   @PostConstruct
   private void init() {
	  try {
		 KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		 keyPairGenerator.initialize(3072);
		 keyPair = keyPairGenerator.generateKeyPair();
	  } catch (Exception e) {
		 throw new IllegalStateException();
	  }
   }

   public PrivateKey getPrivateKey() {
	  return keyPair.getPrivate();
   }

   public RSAPublicKey getPublicKey() {
	  return (RSAPublicKey) keyPair.getPublic();
   }
}