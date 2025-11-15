package org.clau.pizzeriautils.constant;

public final class ApiSecurityResponses {

   private ApiSecurityResponses() {
	  throw new IllegalStateException("Utility class");
   }

   public static final String BAD_CREDENTIALS = "BadCredentialsException";

   public static final String INVALID_TOKEN = "InvalidBearerTokenException";

   public static final String MISSING_TOKEN = "MissingBearerTokenException";
}