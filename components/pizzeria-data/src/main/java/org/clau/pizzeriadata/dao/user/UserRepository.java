package org.clau.pizzeriadata.dao.user;

import org.clau.pizzeriadata.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

   //	@Query("select u from User u join fetch u.roles where u.email = :email")
   Optional<User> findByEmail(String email);
}
