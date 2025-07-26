
package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.service.CustomErrorService;
import org.clau.pizzeriadata.dao.admin.CustomErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomErrorServiceImpl implements CustomErrorService {

   private final CustomErrorRepository errorRepository;

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

   @Override
   public Page<APIError> findAllByOrigin(String origin, int page, int size) {

	  Sort.TypedSort<APIError> error = Sort.sort(APIError.class);
	  Sort sort = error.by(APIError::getId).descending();
	  PageRequest pageRequest = PageRequest.of(page, size, sort);

	  return errorRepository.findAllByOrigin(origin, pageRequest);
   }
}
