package org.clau.pizzeriadata.dao.admin;

import org.clau.pizzeriadata.dao.common.ErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomErrorRepository extends ErrorRepository {

   List<APIError> findAllByOriginAndCreatedOnBetweenOrderByIdDesc(String origin, LocalDateTime createdOnStart, LocalDateTime createdOnEnd);

   List<APIError> findAllByOriginAndCreatedOn(String origin, LocalDateTime createdOn);
}
