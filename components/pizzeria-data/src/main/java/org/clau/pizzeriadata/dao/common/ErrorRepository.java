package org.clau.pizzeriadata.dao.common;

import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<APIError, Long> {
}
