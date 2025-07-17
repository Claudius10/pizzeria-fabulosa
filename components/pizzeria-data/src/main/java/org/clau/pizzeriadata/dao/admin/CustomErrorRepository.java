package org.clau.pizzeriadata.dao.admin;

import org.clau.pizzeriadata.dao.common.ErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomErrorRepository extends ErrorRepository {

   Page<APIError> findAllByOrigin(String origin, Pageable pageable);
}
