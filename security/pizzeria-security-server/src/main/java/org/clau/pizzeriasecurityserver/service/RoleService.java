package org.clau.pizzeriasecurityserver.service;

import org.clau.pizzeriasecurityserver.data.model.Role;

import java.util.Optional;

public interface RoleService {

   void createRole(Role role);

   Optional<Role> findByName(String roleName);
}