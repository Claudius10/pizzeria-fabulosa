package org.clau.pizzeriauserresourceserver.dao;

import org.clau.pizzeriauserassets.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
