package org.clau.pizzeriabackendclient.web.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DefaultController {

	@Value("${angular-app.base-uri}")
	private String appBaseUri;

	@GetMapping("/")
	public String root() {
		return "redirect:" + this.appBaseUri;
	}

	@GetMapping("/logged-in")
	public String loggedIn() {
		// after successful login, the redirect from the authorization server is here
		// to set the JSESSIONID cookie on the domain that is common for both the backend client and angular app,
		// then redirect to the angular app
		return "redirect:" + this.appBaseUri;
	}

//	// '/authorized' is the registered 'redirect_uri' for authorization_code
//	@GetMapping("/authorized")
//	public String authorized() {
//		return "redirect:" + this.appBaseUri;
//	}
}
