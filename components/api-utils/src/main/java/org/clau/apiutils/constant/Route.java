package org.clau.apiutils.constant;

public final class Route {

	private Route() {
		// no init
	}

	// API

	public static final String API = "/api";

	public static final String V1 = "/v1";

	public static final String ALL = "/**";

	// DOCS

	public static final String DOCS = "/docs";

	// ANON

	public static final String ANON_BASE = "/anon";

	public static final String ANON_REGISTER = "/register";

	// RESOURCE

	public static final String RESOURCE = "/resource";

	public static final String PRODUCT_BASE = "/product";

	public static final String PRODUCT_TYPE = "type";

	public static final String STORE_BASE = "/store";

	public static final String OFFER_BASE = "/offer";

	public static final String UTIL_BASE = "/util";

	public static final String LOCAL_DATE_TIME_NOW = "/now";

	// USER

	public static final String USER_BASE = "/user";

	public static final String USER_ID = "/{userId}";
	public static final String USER_ORDER = "/order";
	public static final String USER_ADDRESS_ID = "/{addressId}";

	public static final String USER_ADDRESS = "/address";

	// ORDER

	public static final String ORDER_BASE = "/order";

	public static final String ORDER_ID = "/{orderId}";

	public static final String ORDER_SUMMARY = "/summary";

	// MISC

	public static final String PAGE_NUMBER = "pageNumber";
	public static final String PAGE_SIZE = "pageSize";
	public static final String USER_ID_PARAM = "userId";
}
