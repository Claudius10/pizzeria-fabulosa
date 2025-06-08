package org.clau.pizzeriauserclient.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.ExceptionLogger;
import org.clau.apiutils.util.ServerUtils;
import org.clau.pizzeriauserclient.service.ErrorService;
import org.clau.pizzeriauserclient.util.Constant;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.reactive.function.client.WebClientException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

	private final ErrorService errorService;

	@Override
	protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
		String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

		String cause = body != null ? body.toString() : null;
		String message = "See cause";
		boolean fatal = false;

		APIError error = errorService.create(cause, message, Constant.APP_NAME, path, fatal);

		ResponseDTO response = ResponseDTO.builder()
				.apiError(error)
				.status(HttpStatus.INTERNAL_SERVER_ERROR.value())
				.build();

		return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(WebClientException.class)
	protected ResponseEntity<ResponseDTO> handleWebClientException(WebClientException ex, WebRequest request) {
		boolean fatal = true;
		ResponseDTO response = buildResponse(ex, request, fatal, HttpStatus.INTERNAL_SERVER_ERROR.value());
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ResponseDTO> accessDeniedException(AccessDeniedException ex, WebRequest request) {
		boolean fatal = false;
		ResponseDTO response = buildResponse(ex, request, fatal, HttpStatus.UNAUTHORIZED.value());
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<ResponseDTO> authenticationException(AuthenticationException ex, WebRequest request) {
		boolean fatal = false;
		ResponseDTO response = buildResponse(ex, request, fatal, HttpStatus.UNAUTHORIZED.value());
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ResponseDTO> handleUnknownException(Exception ex, WebRequest request) {
		boolean fatal = true;
		ResponseDTO response = buildResponse(ex, request, fatal, HttpStatus.INTERNAL_SERVER_ERROR.value());
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private ResponseDTO buildResponse(Exception ex, WebRequest request, boolean fatal, int status) {

		HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
		String path = ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());

		String exSimpleName = ex.getClass().getSimpleName();
		String message = ex.getMessage();

		APIError error = errorService.create(
				exSimpleName,
				message,
				Constant.APP_NAME,
				path,
				fatal
		);

		return ResponseDTO.builder()
				.apiError(error)
				.status(status)
				.build();
	}
}
