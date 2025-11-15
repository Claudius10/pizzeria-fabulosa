package org.clau.fabulosa.securityserver.service.impl.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.securityserver.data.dao.UserRepository;
import org.clau.fabulosa.securityserver.data.model.Role;
import org.clau.fabulosa.securityserver.data.model.User;
import org.clau.fabulosa.securityserver.service.user.OidcUserService;
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
		 .claim("roles", user.getRoles().stream().map(Role::getName).toList())
		 .zoneinfo("Europe/Paris")
		 .locale("en-US")
		 .updatedAt("N/A")
		 .build();
   }
}
