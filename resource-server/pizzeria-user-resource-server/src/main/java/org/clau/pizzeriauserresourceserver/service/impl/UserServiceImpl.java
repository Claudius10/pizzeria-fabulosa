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
	  User user = findUserById(userId);
	  checkPasswordMatch(password, user.getPassword());

	  if (user.getEmail().matches(Constant.DUMMY_ACCOUNT_EMAIL)) {
		 throw new IllegalArgumentException(Response.DUMMY_ACCOUNT_ERROR);
	  }

	  userRepository.delete(user);
   }

   @Override
   public void passwordMatches(Long userId, String password) {
	  User user = findUserById(userId);
	  checkPasswordMatch(password, user.getPassword());
   }

   private User findUserById(Long userId) {
	  Optional<User> userById = userRepository.findById(userId);
	  if (userById.isEmpty()) {
		 throw new EntityNotFoundException(Response.USER_NOT_FOUND);
	  }
	  return userById.get();
   }

   private void checkPasswordMatch(String rawPassword, String encodedPassword) {
	  if (!bCrypt.matches(rawPassword, encodedPassword)) {
		 throw new BadCredentialsException(Response.BAD_CREDENTIALS);
	  }
   }
}
