package org.clau.fabulosa.securityserver.service.user;

import org.clau.fabulosa.securityserver.data.model.Role;

import java.util.Optional;

public interface RoleService {

   void createRole(Role role);

   Optional<Role> findByName(String roleName);
}