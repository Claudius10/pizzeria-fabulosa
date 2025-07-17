package org.clau.pizzeriaadminresourceserver;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dao.admin.CustomErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class TestHelperService {

   private final CustomErrorRepository errorRepository;

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
