package org.clau.fabulosa.securityserver.service.impl.setup;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.securityserver.data.dao.UserRepository;
import org.clau.fabulosa.securityserver.data.model.Role;
import org.clau.fabulosa.securityserver.data.model.User;
import org.clau.fabulosa.securityserver.service.user.RoleService;
import org.clau.fabulosa.utils.enums.RoleEnum;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

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
	  if (userRepository.findByEmail("donQuijote@example.com").isEmpty()) {
		 createDummyUser();
	  }
   }

   private void createDummyUser() {

	  Role role = roleService.findByName(RoleEnum.ADMIN.value()).get();

	  User dummyUser = User.builder()
		 .withName("Miguel de Cervantes")
		 .withEmail("donQuijote@example.com")
		 .withContactNumber(123456789)
		 .withRoles(Set.of(role))
		 .withPassword(bCrypt.encode("Password1"))
		 .build();

	  userRepository.save(dummyUser);
   }
}