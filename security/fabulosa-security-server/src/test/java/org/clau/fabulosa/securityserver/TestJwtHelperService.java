package org.clau.fabulosa.securityserver;

import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

   @Autowired
   private final JWKSource<SecurityContext> jwkSource;

   private JwtEncoder jwtEncoder;

   @PostConstruct
   private void buildEncoder() {
	  jwtEncoder = new NimbusJwtEncoder(jwkSource);
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
