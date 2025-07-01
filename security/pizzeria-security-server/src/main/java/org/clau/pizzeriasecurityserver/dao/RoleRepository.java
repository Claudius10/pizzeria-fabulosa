package org.clau.pizzeriasecurityserver.dao;

import org.clau.pizzeriauserassets.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

   Optional<Role> findByName(String name);
}
