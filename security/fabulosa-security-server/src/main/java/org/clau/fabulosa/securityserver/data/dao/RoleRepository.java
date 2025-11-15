package org.clau.fabulosa.securityserver.data.dao;

import org.clau.fabulosa.securityserver.data.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Integer> {

   Optional<Role> findByName(String name);
}