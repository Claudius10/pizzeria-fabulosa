package org.clau.pizzeriasecurityserver.dao;

import org.clau.pizzeriauserassets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   //	@Query("select u from User u join fetch u.roles where u.email = :email")
   Optional<User> findByEmail(String email);
}
