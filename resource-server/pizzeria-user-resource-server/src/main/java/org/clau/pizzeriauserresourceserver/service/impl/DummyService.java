package org.clau.pizzeriauserresourceserver.service.impl;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.user.UserRepository;
import org.clau.pizzeriadata.model.user.Role;
import org.clau.pizzeriadata.model.user.User;
import org.clau.pizzeriauserresourceserver.service.RoleService;
import org.clau.pizzeriautils.constant.user.RoleEnum;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

import static org.clau.pizzeriauserresourceserver.util.Constant.DUMMY_ACCOUNT_EMAIL;

@Profile("production")
@RequiredArgsConstructor
@Service
@Transactional
public class DummyService {

   private final RoleService roleService;

   private final UserRepository userRepository;

   private final PasswordEncoder bCrypt;

   @PostConstruct
   public void init() {
	  setupRoles();
	  setupDummyUser();
   }

   private void setupRoles() {
	  if (roleService.findByName(RoleEnum.USER.value()).isEmpty()) {
		 roleService.createRole(
			Role.builder()
			   .withName(RoleEnum.USER.value())
			   .build()
		 );
	  }

	  if (roleService.findByName(RoleEnum.ADMIN.value()).isEmpty()) {
		 roleService.createRole(
			Role.builder()
			   .withName(RoleEnum.ADMIN.value())
			   .build()
		 );
	  }
   }

   private void setupDummyUser() {
	  if (userRepository.findByEmail(DUMMY_ACCOUNT_EMAIL).isEmpty()) {
		 createDummyUser();
	  }
   }

   private void createDummyUser() {

	  Role role = roleService.findByName(RoleEnum.ADMIN.value()).get();

	  User dummyUser = User.builder()
		 .withName("Miguel de Cervantes")
		 .withEmail(DUMMY_ACCOUNT_EMAIL)
		 .withContactNumber(123456789)
		 .withRoles(Set.of(role))
		 .withPassword(bCrypt.encode("Password1"))
		 .build();

	  userRepository.save(dummyUser);
   }
}