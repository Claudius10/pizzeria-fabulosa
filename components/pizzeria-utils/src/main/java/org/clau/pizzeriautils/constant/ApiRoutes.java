package org.clau.pizzeriautils.constant;

public final class ApiRoutes {

   private ApiRoutes() {
	  throw new IllegalStateException("Utility class");
   }

   public static final String API = "/api";

   public static final String V1 = "/v1";
   public static final String ALL = "/**";
   public static final String ANON_BASE = "/anon";
   public static final String REGISTER = "/register";
   public static final String RESOURCE = "/resource";

   public static final String PRODUCT_BASE = "/product";
   public static final String PRODUCT_TYPE = "type";
   public static final String STORE_BASE = "/store";
   public static final String OFFER_BASE = "/offer";
   public static final String UTIL_BASE = "/util";
   public static final String LOCAL_DATE_TIME_NOW = "/now";
   public static final String USER_BASE = "/user";

   public static final String USER_ID = "/{userId}";
   public static final String ORDER_BASE = "/order";
   public static final String COUNT = "/count";

   public static final String ORDER_ID = "/{orderId}";
   public static final String ORDER_SUMMARY = "/summary";

   public static final String ADMIN_BASE = "/admin";
   public static final String INCIDENTS_BASE = "/incidents";

   public static final String CHECK = "/check";

   public static final String PARAM_PAGE_NUMBER = "pageNumber";
   public static final String PARAM_PAGE_SIZE = "pageSize";
   public static final String PARAM_USER_ID = "userId";
}
