package org.clau.pizzeriabackendclient.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.ExceptionLogger;
import org.clau.apiutils.util.ServerUtils;
import org.clau.pizzeriabackendclient.util.Constant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

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

   @ExceptionHandler(AccessDeniedException.class)
   protected ResponseEntity<ResponseDTO> accessDeniedException(AccessDeniedException ex, WebRequest request) {

	  String cause = ex.getClass().getSimpleName();
	  String message = ex.getMessage();
	  boolean fatal = false;
	  int status = HttpStatus.FORBIDDEN.value();

	  ResponseDTO response = buildResponse(
		 cause,
		 message,
		 request,
		 fatal,
		 status
	  );

	  ExceptionLogger.log(ex, log, response);
	  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
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

	  APIError error = APIError.builder()
		 .withCreatedOn(LocalDateTime.now())
		 .withId(UUID.randomUUID().getMostSignificantBits())
		 .withPath(path)
		 .withMessage(message)
		 .withCause(cause)
		 .withFatal(fatal)
		 .withLogged(false)
		 .withOrigin(Constant.APP_NAME)
		 .build();

	  return ResponseDTO.builder()
		 .apiError(error)
		 .status(status)
		 .build();
   }
}
