package org.clau.fabulosa.backendclient.web.controller;

import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.backendclient.property.MyURI;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class DefaultController {

   private final MyURI uri;

   @GetMapping("/")
   public String root() {
	  return "redirect:" + uri.getAngularBase();
   }

   @GetMapping("/logged-in")
   public String loggedIn() {
	  // after successful login, the redirect from the authorization server is here
	  // to set the JSESSIONID cookie on the domain that is common for both the backend client and angular app,
	  // then redirect to the angular app
	  return "redirect:" + uri.getAngularBase();
   }

//	// '/authorized' is the registered 'redirect_uri' for authorization_code
//	@GetMapping("/authorized")
//	public String authorized() {
//		return "redirect:" + uri.getAngularBase();
//	}
}
