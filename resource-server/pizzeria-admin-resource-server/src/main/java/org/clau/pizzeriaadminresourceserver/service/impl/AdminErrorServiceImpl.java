
package org.clau.pizzeriaadminresourceserver.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriaadminresourceserver.dao.AdminErrorRepository;
import org.clau.pizzeriaadminresourceserver.service.AdminErrorService;
import org.clau.pizzeriadata.model.common.APIError;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AdminErrorServiceImpl implements AdminErrorService {

   private final AdminErrorRepository errorRepository;

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
	  LocalDate start = parseToLocalDate(startDate);
	  LocalDate end = parseToLocalDate(endDate);

	  if (start == null && end == null) {
		 return List.of();
	  }

	  if (start == null) {
		 LocalDateTime dayStart = end.atStartOfDay();
		 LocalDateTime dayEnd = LocalDateTime.of(end, LocalTime.MAX);
		 return errorRepository.findAllByOriginAndCreatedOnBetweenOrderByIdDesc(origin, dayStart, dayEnd);
	  }

	  if (end == null) {
		 LocalDateTime dayStart = start.atStartOfDay();
		 LocalDateTime dayEnd = LocalDateTime.of(start, LocalTime.MAX);
		 return errorRepository.findAllByOriginAndCreatedOnBetweenOrderByIdDesc(origin, dayStart, dayEnd);
	  }

	  LocalDateTime startDateTime = start.atStartOfDay();
	  LocalDateTime endDateTime = LocalDateTime.of(end, LocalTime.MAX);
	  return errorRepository.findAllByOriginAndCreatedOnBetweenOrderByIdDesc(origin, startDateTime, endDateTime);
   }

   private LocalDate parseToLocalDate(String date) {
	  if (date == null || date.isBlank()) {
		 return null;
	  }

	  int tIndex = date.indexOf('T');
	  String onlyDate = (tIndex > 0) ? date.substring(0, tIndex) : date;
	  return LocalDate.parse(onlyDate, DateTimeFormatter.ISO_LOCAL_DATE);
   }
}
