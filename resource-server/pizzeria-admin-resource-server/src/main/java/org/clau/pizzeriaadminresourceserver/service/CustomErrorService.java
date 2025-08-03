package org.clau.pizzeriaadminresourceserver.service;

import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriadata.service.common.ErrorService;

import java.util.List;

public interface CustomErrorService extends ErrorService {

   List<APIError> findAllByOriginBetweenDates(String origin, String startDate, String endDate);
}
