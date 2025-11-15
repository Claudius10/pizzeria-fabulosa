package org.clau.fabulosa.securityserver.service.user;

import org.springframework.security.oauth2.core.oidc.OidcUserInfo;

public interface OidcUserService {

   OidcUserInfo loadUser(String email);
}
