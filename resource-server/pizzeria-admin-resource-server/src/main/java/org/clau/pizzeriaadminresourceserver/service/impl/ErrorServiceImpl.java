package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.dao.ErrorRepository;
import org.clau.pizzeriaadminresourceserver.service.ErrorService;
import org.clau.pizzeriadata.model.common.APIError;
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
