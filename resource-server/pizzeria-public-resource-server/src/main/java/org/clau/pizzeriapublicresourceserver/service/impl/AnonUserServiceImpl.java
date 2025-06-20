package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Response;
import org.clau.pizzeriapublicresourceserver.dao.UserRepository;
import org.clau.pizzeriapublicresourceserver.service.AnonUserService;
import org.clau.pizzeriapublicresourceserver.service.RoleService;
import org.clau.pizzeriapublicassets.dto.RegisterDTO;
import org.clau.pizzeriauserassets.constant.RoleEnum;
import org.clau.pizzeriauserassets.model.Role;
import org.clau.pizzeriauserassets.model.User;
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
   public void createUser(RegisterDTO registerDTO) {
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
