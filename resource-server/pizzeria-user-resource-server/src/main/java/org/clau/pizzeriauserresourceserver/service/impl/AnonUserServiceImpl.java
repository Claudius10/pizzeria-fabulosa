package org.clau.pizzeriauserresourceserver.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.user.UserRepository;
import org.clau.pizzeriadata.model.user.Role;
import org.clau.pizzeriadata.model.user.User;
import org.clau.pizzeriauserresourceserver.service.AnonUserService;
import org.clau.pizzeriauserresourceserver.service.RoleService;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.constant.user.RoleEnum;
import org.clau.pizzeriautils.dto.user.RegisterDTO;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class AnonUserServiceImpl implements AnonUserService {

   private final UserRepository userRepository;

   private final RoleService roleService;

   private final PasswordEncoder bCrypt;

   @Override
   public void create(RegisterDTO registerDTO) {
	  Optional<Role> userRole = roleService.findByName(RoleEnum.USER.value());

	  if (userRole.isEmpty()) {
		 throw new EntityNotFoundException(Response.ROLE_NOT_FOUND);
	  }

	  Set<Role> roles = new HashSet<>();
	  roles.add(userRole.get());

	  String password = bCrypt.encode(registerDTO.password());

	  User user = User.builder()
		 .withName(registerDTO.name())
		 .withEmail(registerDTO.email())
		 .withContactNumber(registerDTO.contactNumber())
		 .withPassword(password)
		 .withRoles(roles)
		 .build();

	  userRepository.save(user);
   }
}
