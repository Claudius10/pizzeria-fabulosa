package org.clau.pizzeriapublicresourceserver.exception;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriadata.service.common.ErrorService;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.util.common.ExceptionLogger;
import org.clau.pizzeriautils.util.common.ServerUtils;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.util.common.constant.MyApps;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

   private final ErrorService errorService;

   @Override
   protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

	  boolean fatal = false;
	  String cause = body != null ? body.toString() : null;
	  String message = "See cause";

	  ResponseDTO response = buildResponse(
		 cause,
		 message,
		 request,
		 fatal,
		 statusCode.value()
	  );

	  return new ResponseEntity<>(response, headers, statusCode);
   }

   @Override
   protected ResponseEntity<Object> handleMethodArgumentNotValid(
	  MethodArgumentNotValidException ex,
	  HttpHeaders headers,
	  HttpStatusCode status,
	  WebRequest request
   ) {

	  HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
	  String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

	  String cause = ex.getClass().getSimpleName();
	  List<String> errorMessages = new ArrayList<>();

	  ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
		 errorMessages.add(String.format("Field: %s - Error: %s - Value: %s",
			fieldError.getField(),
			fieldError.getDefaultMessage(),
			fieldError.getRejectedValue()));
	  });

	  ResponseDTO response = ResponseDTO.builder()
		 .apiError(APIError.builder()
			.withId(UUID.randomUUID().getMostSignificantBits())
			.withCreatedOn(TimeUtils.getNowAccountingDST())
			.withCause(cause)
			.withMessage(String.valueOf(errorMessages))
			.withOrigin(MyApps.RESOURCE_SERVER_PUBLIC)
			.withPath(path)
			.withLogged(false)
			.withFatal(false)
			.build())
		 .status(HttpStatus.BAD_REQUEST.value())
		 .build();

	  ExceptionLogger.log(ex, log, response);
	  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
   }

   @ExceptionHandler(PersistenceException.class)
   protected ResponseEntity<ResponseDTO> handlePersistenceException(PersistenceException ex, WebRequest request) {

	  boolean fatal = true;
	  String cause = ex.getClass().getSimpleName();
	  String message = ex.getMessage();

	  ResponseDTO response = buildResponse(
		 cause,
		 message,
		 request,
		 fatal,
		 HttpStatus.INTERNAL_SERVER_ERROR.value()
	  );

	  ExceptionLogger.log(ex, log, response);

	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
   }

   @ExceptionHandler(Exception.class)
   protected ResponseEntity<ResponseDTO> handleUnknownException(Exception ex, WebRequest request) {

	  boolean fatal = true;
	  String cause = ex.getClass().getSimpleName();
	  String message = ex.getMessage();

	  ResponseDTO response = buildResponse(
		 cause,
		 message,
		 request,
		 fatal,
		 HttpStatus.INTERNAL_SERVER_ERROR.value()
	  );

	  ExceptionLogger.log(ex, log, response);

	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
   }

   private ResponseDTO buildResponse(String cause, String message, WebRequest request, boolean fatal, int status) {

	  HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
	  String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

	  APIError error = errorService.create(
		 cause,
		 message,
		 MyApps.RESOURCE_SERVER_PUBLIC,
		 path,
		 fatal
	  );

	  return ResponseDTO.builder()
		 .apiError(error)
		 .status(status)
		 .build();
   }
}
