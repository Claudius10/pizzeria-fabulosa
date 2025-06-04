package org.clau.pizzeriabusinessclient.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.ExceptionLogger;
import org.clau.apiutils.util.ServerUtils;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessclient.service.ErrorService;
import org.clau.pizzeriabusinessclient.util.Constant;
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
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.UUID;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class MyExceptionHandler extends ResponseEntityExceptionHandler {

	private final ErrorService errorService;

	@Override
	protected ResponseEntity<Object> createResponseEntity(@Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

		String path = extractPath(request);

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

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ResponseDTO> handleUnknownException(Exception ex, WebRequest request) {
		ResponseDTO response = buildUnknownException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ResponseDTO> accessDeniedException(AccessDeniedException ex, WebRequest request) {

		String path = extractPath(request);
		ResponseDTO response = buildResponse(ex, path);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<ResponseDTO> authenticationException(AuthenticationException ex, WebRequest request) {

		String path = extractPath(request);
		ResponseDTO response = buildResponse(ex, path);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	private ResponseDTO buildResponse(RuntimeException ex, String path) {

		String exSimpleName = ex.getClass().getSimpleName();

		APIError error = APIError.builder()
				.withId(UUID.randomUUID().getMostSignificantBits())
				.withCreatedOn(TimeUtils.getNowAccountingDST())
				.withCause(exSimpleName)
				.withMessage(ex.getMessage())
				.withOrigin(Constant.APP_NAME)
				.withPath(path)
				.withLogged(false)
				.withFatal(false)
				.build();

		return ResponseDTO.builder()
				.apiError(error)
				.status(HttpStatus.UNAUTHORIZED.value())
				.build();
	}

	private ResponseDTO buildUnknownException(Exception ex, WebRequest request, HttpStatus status) {
		String path = extractPath(request);

		String cause = ex.getClass().getSimpleName();
		String message = ex.getMessage();
		boolean fatal = true;

		APIError error = errorService.create(cause, message, Constant.APP_NAME, path, fatal);

		return ResponseDTO.builder()
				.apiError(error)
				.status(status.value())
				.build();
	}

	private String extractPath(WebRequest request) {
		HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
		return ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());
	}
}
