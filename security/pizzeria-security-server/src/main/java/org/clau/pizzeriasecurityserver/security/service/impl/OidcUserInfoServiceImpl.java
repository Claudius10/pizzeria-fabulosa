package org.clau.pizzeriasecurityserver.security.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.security.dao.UserRepository;
import org.clau.pizzeriasecurityserver.security.service.OidcUserService;
import org.clau.pizzeriauserassets.model.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OidcUserInfoServiceImpl implements OidcUserService {

	private final UserRepository userRepository;

	@Override
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
				.claim("address", user.getAddress())
				.claim("id", user.getId())
				.zoneinfo("Europe/Paris")
				.locale("en-US")
				.updatedAt("N/A")
				.build();
	}
}
