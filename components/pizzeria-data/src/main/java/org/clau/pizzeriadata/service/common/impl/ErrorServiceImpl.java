package org.clau.pizzeriadata.service.common.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.common.ErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriadata.service.common.ErrorService;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
public class ErrorServiceImpl implements ErrorService {

   private final ErrorRepository errorRepository;

   @Override
   public APIError create(String cause, String message, String origin, String uriPath, boolean fatal) {

	  APIError error = APIError.builder()
		 .withCreatedOn(LocalDateTime.now())
		 .withCause(cause)
		 .withMessage(message)
		 .withOrigin(origin)
		 .withPath(uriPath)
		 .withLogged(true)
		 .withFatal(fatal)
		 .build();

	  return errorRepository.save(error);
   }
}
