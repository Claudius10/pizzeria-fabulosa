package org.clau.pizzeriaadminresourceserver.service;

import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriadata.service.common.ErrorService;
import org.springframework.data.domain.Page;

public interface CustomErrorService extends ErrorService {

   Page<APIError> findAllByOrigin(String origin, int page, int size);
}
