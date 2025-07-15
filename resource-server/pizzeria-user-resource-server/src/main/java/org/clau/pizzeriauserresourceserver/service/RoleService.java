package org.clau.pizzeriauserresourceserver.service;

import org.clau.pizzeriadata.model.user.Role;

import java.util.Optional;

public interface RoleService {

   void createRole(Role role);

   Optional<Role> findByName(String roleName);
}