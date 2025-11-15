package org.clau.pizzeriasecurityserver.service.impl.user;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.data.dao.UserRepository;
import org.clau.pizzeriasecurityserver.data.model.User;
import org.clau.pizzeriasecurityserver.service.CacheService;
import org.clau.pizzeriasecurityserver.service.user.UserService;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
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

   private final CacheService cacheService;

   @Override
   public void deleteById(Long userId, String password) {
	  User user = findUserById(userId);
	  checkPasswordMatch(password, user.getPassword());

	  if (user.getEmail().matches("donQuijote@example.com")) {
		 throw new IllegalArgumentException(ApiResponseMessages.DUMMY_ACCOUNT_ERROR);
	  }

	  userRepository.delete(user);
	  cacheService.evict("oidc-user-info", user.getEmail());
   }

   @Override
   public void passwordMatches(Long userId, String password) {
	  User user = findUserById(userId);
	  checkPasswordMatch(password, user.getPassword());
   }

   private User findUserById(Long userId) {
	  Optional<User> userById = userRepository.findById(userId);
	  if (userById.isEmpty()) {
		 throw new EntityNotFoundException(ApiResponseMessages.USER_NOT_FOUND);
	  }
	  return userById.get();
   }

   private void checkPasswordMatch(String rawPassword, String encodedPassword) {
	  if (!bCrypt.matches(rawPassword, encodedPassword)) {
		 throw new BadCredentialsException(ApiResponseMessages.BAD_CREDENTIALS);
	  }
   }
}
