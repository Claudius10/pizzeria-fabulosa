package org.clau.pizzeriasecurityserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriasecurityserver.dao.RoleRepository;
import org.clau.pizzeriasecurityserver.service.RoleService;
import org.clau.pizzeriauserassets.model.Role;
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