package org.clau.pizzeriaassetsresourceserver.dao;

import org.clau.apiutils.model.APIError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<APIError, Long> {
}
