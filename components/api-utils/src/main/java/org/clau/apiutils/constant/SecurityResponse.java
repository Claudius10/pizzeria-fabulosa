package org.clau.apiutils.constant;

public final class SecurityResponse {

	public static final String BAD_CREDENTIALS = "BadCredentialsException";

	public static final String INVALID_TOKEN = "InvalidBearerTokenException";

	public static final String MISSING_TOKEN = "MissingBearerTokenException";

	private SecurityResponse() {
		// no init
	}
}