package org.clau.pizzeriasecurityserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.data.dao.UserRepository;
import org.clau.pizzeriasecurityserver.data.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthenticationServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  Optional<User> user = userRepository.findByEmail(username);
	  return user.orElseThrow(() -> new UsernameNotFoundException(username)); // this ends up as AuthenticationException
   }
}