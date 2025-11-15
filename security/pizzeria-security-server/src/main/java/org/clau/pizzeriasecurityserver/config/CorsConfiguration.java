package org.clau.pizzeriasecurityserver.config;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.property.MyURI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Configuration
public class CorsConfiguration {

   private final MyURI uri;

   @Bean
   CorsConfigurationSource corsConfigurationSource() {
	  org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();

	  config.setAllowedOrigins(uri.getAllowedOrigins());
	  config.addAllowedHeader("X-XSRF-TOKEN");
	  config.addAllowedHeader(HttpHeaders.CONTENT_TYPE);
	  config.setExposedHeaders(List.of(HttpHeaders.WWW_AUTHENTICATE));
	  config.setAllowedMethods(Arrays.asList("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS"));
	  config.setAllowCredentials(true);

	  UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	  // only register CORS for frontend-accessed endpoints
	  source.registerCorsConfiguration("/login/**", config);
	  source.registerCorsConfiguration("/oauth2/**", config);
	  source.registerCorsConfiguration("/.well-known/**", config);
	  source.registerCorsConfiguration("/authorize/**", config);
	  source.registerCorsConfiguration("/error/**", config);
	  source.registerCorsConfiguration("/api/v1/register/**", config);

	  return source;
   }
}
