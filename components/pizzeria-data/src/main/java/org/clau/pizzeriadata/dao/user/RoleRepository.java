package org.clau.pizzeriadata.dao.user;

import org.clau.pizzeriadata.model.user.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

   Optional<Role> findByName(String name);
}