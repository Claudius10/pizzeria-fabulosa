package org.clau.fabulosa.securityserver.data.dao;

import org.clau.fabulosa.data.model.common.APIError;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ErrorRepository extends JpaRepository<APIError, Long> {
}
