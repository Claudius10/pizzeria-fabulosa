package org.clau.pizzeriauserresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.user.RoleRepository;
import org.clau.pizzeriadata.model.user.Role;
import org.clau.pizzeriauserresourceserver.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleServiceImpl implements RoleService {

   private final RoleRepository roleRepository;

   @Override
   public void createRole(Role role) {
	  this.roleRepository.save(role);
   }

   @Override
   public Optional<Role> findByName(String roleName) {
	  return roleRepository.findByName(roleName);
   }
}