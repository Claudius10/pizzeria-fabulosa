package org.clau.pizzeriaadminresourceserver.data.dao;

import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AdminErrorRepository extends JpaRepository<APIError, Long> {

   List<APIError> findAllByOriginAndCreatedOnBetweenOrderByIdDesc(String origin, LocalDateTime createdOnStart, LocalDateTime createdOnEnd);
}
