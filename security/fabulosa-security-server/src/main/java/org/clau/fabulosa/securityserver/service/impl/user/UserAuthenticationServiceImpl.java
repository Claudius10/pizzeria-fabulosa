package org.clau.fabulosa.securityserver.service.impl.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.securityserver.data.dao.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserAuthenticationServiceImpl implements UserDetailsService {

   private final UserRepository userRepository;

   @Override
   public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
	  return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException(username)); // this ends up as AuthenticationException
   }
}