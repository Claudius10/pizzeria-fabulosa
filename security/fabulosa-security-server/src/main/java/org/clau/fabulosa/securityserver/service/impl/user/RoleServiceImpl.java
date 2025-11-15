package org.clau.fabulosa.securityserver.service.impl.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.securityserver.data.dao.RoleRepository;
import org.clau.fabulosa.securityserver.data.model.Role;
import org.clau.fabulosa.securityserver.service.user.RoleService;
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