package org.clau.pizzeriasecurityserver.security.service;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface OidcUserService {

	OidcUserInfo loadUser(String email);
}
