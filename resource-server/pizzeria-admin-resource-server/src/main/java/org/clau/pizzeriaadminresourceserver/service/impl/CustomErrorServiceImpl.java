
package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.service.CustomErrorService;
import org.clau.pizzeriadata.dao.admin.CustomErrorRepository;
import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
   public List<APIError> findAllByOriginBetweenDates(String origin, String startDate, String endDate) {
	  LocalDateTime startDateTime = parseDate(startDate);
	  LocalDateTime endDateTime = parseDate(endDate);

	  if (startDateTime == null && endDateTime == null) {
		 return List.of();
	  }

	  if (startDateTime == null) {
		 return errorRepository.findAllByOriginAndCreatedOn(origin, endDateTime);
	  }

	  if (endDateTime == null) {
		 return errorRepository.findAllByOriginAndCreatedOn(origin, startDateTime);
	  }

	  return errorRepository.findAllByOriginAndCreatedOnBetweenOrderByIdDesc(origin, startDateTime, endDateTime);
   }

   private LocalDateTime parseDate(String date) {
	  if (date == null || date.isBlank()) {
		 return null;
	  }

	  String cleanDate = date.substring(0, date.indexOf('T')) + "T00:00:00";
	  return LocalDateTime.parse(cleanDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
   }
}
