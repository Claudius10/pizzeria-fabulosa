package org.clau.fabulosa.securityserver.data.dao;

import org.clau.fabulosa.securityserver.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   //	@Query("select u from User u join fetch u.roles where u.email = :email")
   Optional<User> findByEmail(String email);
}
