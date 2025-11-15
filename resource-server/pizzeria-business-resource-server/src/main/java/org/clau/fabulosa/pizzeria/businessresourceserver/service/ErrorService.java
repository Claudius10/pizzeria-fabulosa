package org.clau.fabulosa.pizzeria.businessresourceserver.service;

import org.clau.fabulosa.data.model.common.APIError;

public interface ErrorService {

   APIError create(String cause, String message, String origin, String uriPath, boolean fata);
}
