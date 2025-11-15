package org.clau.pizzeriaadminresourceserver.service;

import org.clau.pizzeriadata.model.common.APIError;

import java.util.List;

public interface AdminErrorService extends ErrorService {

   List<APIError> findAllByOriginBetweenDates(String origin, String startDate, String endDate);
}
