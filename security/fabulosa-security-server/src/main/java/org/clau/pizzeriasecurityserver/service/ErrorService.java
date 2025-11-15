package org.clau.pizzeriasecurityserver.service;

import org.clau.pizzeriadata.model.common.APIError;

public interface ErrorService {

   APIError create(String cause, String message, String origin, String uriPath, boolean fata);
}
