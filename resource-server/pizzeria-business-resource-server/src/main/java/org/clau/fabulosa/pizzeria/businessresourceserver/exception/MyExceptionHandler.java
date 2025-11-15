package org.clau.fabulosa.pizzeria.businessresourceserver.exception;

import jakarta.persistence.PersistenceException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.fabulosa.pizzeria.businessresourceserver.service.ErrorService;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.data.model.common.APIError;
import org.clau.fabulosa.utils.constant.ApiSecurityResponses;
import org.clau.fabulosa.utils.constant.MyApps;
import org.clau.fabulosa.utils.logger.ExceptionLogger;
import org.clau.fabulosa.utils.util.ServerUtils;
import org.clau.fabulosa.utils.util.TimeUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
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

	  APIError apiError = saveError(cause, message, request, fatal);
	  ResponseDTO response = buildResponse(apiError, statusCode.value());

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
			.withOrigin(MyApps.RESOURCE_SERVER_BUSINESS)
			.withPath(path)
			.withLogged(false)
			.withFatal(false)
			.build())
		 .status(HttpStatus.BAD_REQUEST.value())
		 .build();

	  ExceptionLogger.log(ex, log);
	  return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
   }

   @ExceptionHandler(PersistenceException.class)
   protected ResponseEntity<ResponseDTO> handlePersistenceException(PersistenceException ex, WebRequest request) {

	  boolean fatal = true;
	  String cause = ex.getClass().getSimpleName();
	  String message = ex.getMessage();

	  APIError apiError = saveError(cause, message, request, fatal);
	  ResponseDTO response = buildResponse(apiError, HttpStatus.INTERNAL_SERVER_ERROR.value());

	  ExceptionLogger.log(ex, log);

	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
   }

   @ExceptionHandler(AuthenticationException.class)
   protected ResponseEntity<ResponseDTO> authenticationException(AuthenticationException ex, WebRequest request) {
	  // AuthenticationException example -> missing or invalid token value

	  ResponseDTO response = handleAuthenticationException(ex, request);

	  ExceptionLogger.log(ex, log);
	  return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
   }

   @ExceptionHandler(AccessDeniedException.class)
   protected ResponseEntity<ResponseDTO> accessDeniedException(AccessDeniedException ex, WebRequest request) {
	  // AccessDeniedException example -> when the scope claim does not contain the required authority

	  ResponseDTO response = handleAccessDenied(ex, request);

	  ExceptionLogger.log(ex, log);
	  return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
   }

   @ExceptionHandler(Exception.class)
   protected ResponseEntity<ResponseDTO> handleUnknownException(Exception ex, WebRequest request) {

	  boolean fatal = true;
	  String cause = ex.getClass().getSimpleName();
	  String message = ex.getMessage();

	  APIError apiError = saveError(cause, message, request, fatal);
	  ResponseDTO response = buildResponse(apiError, HttpStatus.INTERNAL_SERVER_ERROR.value());

	  ExceptionLogger.log(ex, log);

	  return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
   }

   private ResponseDTO handleAuthenticationException(AuthenticationException ex, WebRequest request) {

	  HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
	  String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

	  APIError error;
	  String cause = ex.getClass().getSimpleName();
	  String message;
	  boolean fatal = false;
	  boolean logged = false;

	  switch (ex) {
		 case InsufficientAuthenticationException ignored -> message = ApiSecurityResponses.MISSING_TOKEN;
		 case InvalidBearerTokenException ignored -> message = ApiSecurityResponses.INVALID_TOKEN;
		 case BadCredentialsException ignored -> message = ApiSecurityResponses.BAD_CREDENTIALS;
		 default -> {
			fatal = true;
			logged = true;
			message = ex.getMessage();
		 }
	  }

	  if (logged) {
		 error = errorService.create(cause, message, MyApps.RESOURCE_SERVER_BUSINESS, path, fatal);
	  } else {
		 error = APIError.builder()
			.withId(UUID.randomUUID().getMostSignificantBits())
			.withCreatedOn(TimeUtils.getNowAccountingDST())
			.withCause(cause)
			.withMessage(message)
			.withOrigin(MyApps.RESOURCE_SERVER_BUSINESS)
			.withPath(path)
			.withLogged(logged)
			.withFatal(fatal)
			.build();
	  }

	  return ResponseDTO.builder()
		 .apiError(error)
		 .status(HttpStatus.UNAUTHORIZED.value())
		 .build();
   }

   private ResponseDTO handleAccessDenied(AccessDeniedException ex, WebRequest request) {

	  HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
	  String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

	  APIError error = APIError.builder()
		 .withId(UUID.randomUUID().getMostSignificantBits())
		 .withCreatedOn(TimeUtils.getNowAccountingDST())
		 .withCause(ex.getClass().getSimpleName())
		 .withMessage(ex.getMessage())
		 .withOrigin(MyApps.RESOURCE_SERVER_BUSINESS)
		 .withPath(path)
		 .withLogged(false)
		 .withFatal(false)
		 .build();

	  return ResponseDTO.builder()
		 .apiError(error)
		 .status(HttpStatus.FORBIDDEN.value())
		 .build();
   }

   private APIError saveError(String cause, String message, WebRequest request, boolean fatal) {
	  HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
	  String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());
	  return errorService.create(
		 cause,
		 message,
		 MyApps.SECURITY_SERVER,
		 path,
		 fatal
	  );
   }

   private ResponseDTO buildResponse(APIError error, int status) {
	  return ResponseDTO.builder()
		 .apiError(error)
		 .status(status)
		 .build();
   }
}
