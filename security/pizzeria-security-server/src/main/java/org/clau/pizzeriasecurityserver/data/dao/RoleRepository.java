package org.clau.pizzeriasecurityserver.data.dao;

import org.clau.pizzeriasecurityserver.data.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

   Optional<Role> findByName(String name);
}