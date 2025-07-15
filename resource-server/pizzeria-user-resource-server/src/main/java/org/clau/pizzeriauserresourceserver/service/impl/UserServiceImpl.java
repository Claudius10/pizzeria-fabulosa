package org.clau.pizzeriauserresourceserver.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.user.UserRepository;
import org.clau.pizzeriadata.model.user.User;
import org.clau.pizzeriauserresourceserver.service.UserService;
import org.clau.pizzeriauserresourceserver.util.Constant;
import org.clau.pizzeriautils.constant.common.Response;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

   private final UserRepository userRepository;

   private final PasswordEncoder bCrypt;

   @Override
   public void deleteById(Long userId, String password) {

	  Optional<User> userById = userRepository.findById(userId);
	  if (userById.isEmpty()) {
		 throw new EntityNotFoundException(Response.USER_NOT_FOUND);
	  }

	  User user = userById.get();

	  if (user.getEmail().matches(Constant.DUMMY_ACCOUNT_EMAIL)) {
		 throw new IllegalArgumentException(Response.DUMMY_ACCOUNT_ERROR);
	  }

	  boolean match = bCrypt.matches(password, user.getPassword());
	  if (!match) {
		 throw new BadCredentialsException(Response.BAD_CREDENTIALS);
	  }

	  userRepository.delete(user);
   }
}
