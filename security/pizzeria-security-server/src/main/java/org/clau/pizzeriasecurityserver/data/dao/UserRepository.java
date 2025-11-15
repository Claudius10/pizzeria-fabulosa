package org.clau.pizzeriasecurityserver.data.dao;

import org.clau.pizzeriasecurityserver.data.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   //	@Query("select u from User u join fetch u.roles where u.email = :email")
   Optional<User> findByEmail(String email);
}
