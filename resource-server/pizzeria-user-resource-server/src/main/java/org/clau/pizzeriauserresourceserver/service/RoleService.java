package org.clau.pizzeriauserresourceserver.service;

import org.clau.pizzeriauserassets.model.Role;

import java.util.Optional;

public interface RoleService {

   void createRole(Role role);

   Optional<Role> findByName(String roleName);
}