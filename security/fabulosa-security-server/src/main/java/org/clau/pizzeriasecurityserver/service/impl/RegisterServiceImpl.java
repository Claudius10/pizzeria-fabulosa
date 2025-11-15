package org.clau.pizzeriasecurityserver.service.impl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.data.dao.UserRepository;
import org.clau.pizzeriasecurityserver.data.dto.RegisterDTO;
import org.clau.pizzeriasecurityserver.data.model.Role;
import org.clau.pizzeriasecurityserver.data.model.User;
import org.clau.pizzeriasecurityserver.service.RegisterService;
import org.clau.pizzeriasecurityserver.service.user.RoleService;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.clau.pizzeriautils.enums.RoleEnum;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class RegisterServiceImpl implements RegisterService {

   private final UserRepository userRepository;

   private final RoleService roleService;

   private final PasswordEncoder bCrypt;

   @Override
   public void create(RegisterDTO registerDTO) {
	  Optional<Role> userRole = roleService.findByName(RoleEnum.USER.value());

	  if (userRole.isEmpty()) {
		 throw new EntityNotFoundException(ApiResponseMessages.ROLE_NOT_FOUND);
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
