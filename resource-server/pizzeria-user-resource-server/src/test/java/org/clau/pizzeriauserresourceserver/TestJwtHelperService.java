package org.clau.pizzeriauserresourceserver;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TestJwtHelperService {

   private final JWTKeys keys;

   private JwtEncoder jwtEncoder;

   @PostConstruct
   private void buildEncoder() {
	  JWK jwk = new RSAKey.Builder(keys.getPublicKey()).privateKey(keys.getPrivateKey()).build();
	  JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
	  jwtEncoder = new NimbusJwtEncoder(jwks);
   }

   public String generateAccessToken(List<String> roles) {
	  JwtClaimsSet claims = JwtClaimsSet.builder()
		 .issuedAt(Instant.now())
		 .issuer("http://127.0.0.1:9000")
		 .expiresAt(Instant.now().plus(1, ChronoUnit.MINUTES))
		 .claim("roles", String.join(" ", roles))
		 .build();
	  return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
   }
}
