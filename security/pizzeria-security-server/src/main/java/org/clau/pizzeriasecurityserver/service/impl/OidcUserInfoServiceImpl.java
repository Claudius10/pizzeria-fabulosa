package org.clau.pizzeriasecurityserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.user.UserRepository;
import org.clau.pizzeriadata.model.user.User;
import org.clau.pizzeriasecurityserver.service.OidcUserService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OidcUserInfoServiceImpl implements OidcUserService {

   private final UserRepository userRepository;

   @Cacheable(value = "oidc-user-info", key = "#email")
   public OidcUserInfo loadUser(String email) {
	  Optional<User> user = userRepository.findByEmail(email);
	  return user.map(this::createUserInfo).orElseThrow(() -> new UsernameNotFoundException(email));
   }

   private OidcUserInfo createUserInfo(User user) {
	  return OidcUserInfo.builder()
		 .subject(user.getEmail())
		 .name(user.getName())
		 .email(user.getEmail())
		 .emailVerified(false)
		 .phoneNumber(String.valueOf(user.getContactNumber()))
		 .phoneNumberVerified(false)
		 .claim("id", user.getId())
		 .zoneinfo("Europe/Paris")
		 .locale("en-US")
		 .updatedAt("N/A")
		 .build();
   }
}
