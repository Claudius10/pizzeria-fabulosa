package org.clau.apiutils.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

public final class SecurityCookies {

	public static final String ACCESS_TOKEN = "Pizzeria.Fabulosa.ACCESS_TOKEN";

	public static final String ID_TOKEN = "Pizzeria.Fabulosa.ID_TOKEN";

	private SecurityCookies() {
	}

	public static Cookie prepareCookie(String name, String value, int maxAge, boolean httpOnly, boolean secure) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/");
		cookie.setMaxAge(maxAge);
		cookie.setHttpOnly(httpOnly);
		cookie.setSecure(secure);
		return cookie;
	}

	public static ResponseCookie bakeCookie(
			String name,
			String value,
			int maxAge,
			boolean httpOnly,
			boolean secure,
			String sameSite,
			String domain) {
		return ResponseCookie.from(name, value)
				.path("/")
				.maxAge(maxAge)
				.httpOnly(httpOnly)
				.secure(secure)
				.sameSite(sameSite)
				.domain(domain)
				.build();
	}

	public static void serveCookies(HttpServletResponse response, String name, String value, int duration,
									boolean httpOnly, boolean secure, String sameSite, String domain) {
		response.addHeader(HttpHeaders.SET_COOKIE,
				bakeCookie(
						name,
						value,
						duration,
						httpOnly,
						secure,
						sameSite,
						domain)
						.toString());
	}

	public static void eatAllCookies(HttpServletRequest request, HttpServletResponse response, String domain) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(ACCESS_TOKEN) || cookie.getName().equals(ID_TOKEN)) {
					cookie.setSecure(false);
					cookie.setDomain(domain);
					cookie.setValue("");
					cookie.setPath("/");
					cookie.setMaxAge(0);
					response.addCookie(cookie);
				}
			}
	}
}