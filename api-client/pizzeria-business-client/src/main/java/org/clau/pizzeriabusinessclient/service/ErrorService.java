package org.clau.pizzeriabusinessclient.service;

import org.clau.apiutils.model.APIError;

public interface ErrorService {

	APIError create(String cause, String message, String origin, String uriPath, boolean fata);
}
