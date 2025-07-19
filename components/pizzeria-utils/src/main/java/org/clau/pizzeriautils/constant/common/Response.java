package org.clau.pizzeriautils.constant.common;

public final class Response {

   public static final String USER_EMAIL_ALREADY_EXISTS = "EmailAlreadyExists";

   public static final String ROLE_NOT_FOUND = "RoleNotFound";

   public static final String DUMMY_ACCOUNT_ERROR = "DummyAccountError";

   public static final String USER_NOT_FOUND = "UserNotFound";

   public static final String BAD_CREDENTIALS = "BadCredentialsException";

   public static final String JSON = "application/json";
   public static final String TEXT = "text";

   public static final String OK = "200";
   public static final String CREATED = "201";
   public static final String NO_CONTENT = "204";
   public static final String BAD_REQUEST = "400";
   public static final String UNAUTHORIZED = "401";
   public static final String INTERNAL_SERVER_ERROR = "500";

   private Response() {
	  // no init
   }
}
