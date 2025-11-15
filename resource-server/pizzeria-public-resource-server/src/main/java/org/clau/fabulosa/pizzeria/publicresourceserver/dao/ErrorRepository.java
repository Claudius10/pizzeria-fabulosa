package org.clau.fabulosa.pizzeria.publicresourceserver.dao;

import org.clau.fabulosa.data.model.common.APIError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<APIError, Long> {
}
