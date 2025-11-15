package org.clau.pizzeriapublicresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriapublicresourceserver.dao.ErrorRepository;
import org.clau.pizzeriapublicresourceserver.service.ErrorService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
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
