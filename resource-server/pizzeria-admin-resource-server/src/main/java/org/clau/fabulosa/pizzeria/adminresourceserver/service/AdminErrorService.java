package org.clau.fabulosa.pizzeria.adminresourceserver.service;

import org.clau.fabulosa.data.model.common.APIError;

import java.util.List;

public interface AdminErrorService extends ErrorService {

   List<APIError> findAllByOriginBetweenDates(String origin, String startDate, String endDate);
}
