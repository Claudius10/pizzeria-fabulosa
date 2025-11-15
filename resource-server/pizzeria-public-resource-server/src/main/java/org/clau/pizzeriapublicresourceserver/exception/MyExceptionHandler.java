package org.clau.pizzeriapublicresourceserver.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriapublicresourceserver.service.ErrorService;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.logger.ExceptionLogger;
import org.clau.pizzeriautils.util.ServerUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
