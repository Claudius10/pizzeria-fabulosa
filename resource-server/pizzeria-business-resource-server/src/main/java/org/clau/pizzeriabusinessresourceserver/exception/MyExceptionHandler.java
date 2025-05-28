package org.clau.pizzeriabusinessresourceserver.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.apiutils.constant.SecurityResponse;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.service.ErrorService;
import org.clau.apiutils.util.ExceptionLogger;
import org.clau.apiutils.util.SecurityCookies;
import org.clau.apiutils.util.ServerUtils;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${domain:192.168.1.128}")
	private String domain;

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
				.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.build();

		return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ResponseDTO> handleUnknownException(Exception ex, WebRequest request) {
		ResponseDTO response = buildUnknownException(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	}

	private String extractPath(WebRequest request) {
		HttpServletRequest httpRequest = ((ServletWebRequest) request).getRequest();
		return ServerUtils.resolvePath(httpRequest.getServletPath(), httpRequest.getRequestURI());
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(
			MethodArgumentNotValidException ex,
			HttpHeaders headers,
			HttpStatusCode status,
			WebRequest request
	) {

		String path = extractPath(request);
		String exSimpleName = ex.getClass().getSimpleName();
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
						.withCause(exSimpleName)
						.withMessage(String.valueOf(errorMessages))
						.withOrigin(Constant.APP_NAME)
						.withPath(path)
						.withLogged(false)
						.withFatal(false)
						.build())
				.status(HttpStatus.BAD_REQUEST)
				.build();

		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}

	@ExceptionHandler(AccessDeniedException.class)
	protected ResponseEntity<ResponseDTO> accessDeniedException(AccessDeniedException ex, WebRequest request) {

		// AccessDeniedException example -> when the scope claim does not contain the required authority

		String path = extractPath(request);
		ResponseDTO response = handleAccessDenied(ex, path);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	@ExceptionHandler(AuthenticationException.class)
	protected ResponseEntity<ResponseDTO> authenticationException(AuthenticationException ex, WebRequest request) {

		// AuthenticationException example -> missing or invalid token value

		String path = extractPath(request);
		ResponseDTO response = handleAuthenticationException(ex, request, path);
		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
	}

	private ResponseDTO handleAccessDenied(AccessDeniedException ex, String path) {

		String exSimpleName = ex.getClass().getSimpleName();

		APIError error = APIError.builder()
				.withId(UUID.randomUUID().getMostSignificantBits())
				.withCreatedOn(TimeUtils.getNowAccountingDST())
				.withCause(exSimpleName)
				.withMessage(ex.getMessage())
				.withOrigin(Constant.APP_NAME)
				.withPath(path)
				.withLogged(false)
				.withFatal(true)
				.build();

		return ResponseDTO.builder()
				.apiError(error)
				.status(HttpStatus.UNAUTHORIZED)
				.build();
	}

	private ResponseDTO handleAuthenticationException(AuthenticationException ex, WebRequest request, String path) {

		String exSimpleName = ex.getClass().getSimpleName();
		String errorMessage;

		boolean fatal = false;
		boolean logged = false;
		boolean deleteCookies = false;

		switch (ex) {
			case InsufficientAuthenticationException ignored -> errorMessage = SecurityResponse.MISSING_TOKEN;
			case BadCredentialsException ignored -> errorMessage = SecurityResponse.BAD_CREDENTIALS;
			case InvalidBearerTokenException ignored -> {
				errorMessage = SecurityResponse.INVALID_TOKEN;
				deleteCookies = true;
			}
			default -> {
				fatal = true;
				logged = true;
				errorMessage = ex.getMessage();
			}
		}

		APIError error;
		if (logged) {
			error = errorService.create(exSimpleName, errorMessage, Constant.APP_NAME, path, fatal);
		} else {
			error = APIError.builder()
					.withId(UUID.randomUUID().getMostSignificantBits())
					.withCreatedOn(TimeUtils.getNowAccountingDST())
					.withCause(exSimpleName)
					.withMessage(errorMessage)
					.withOrigin(Constant.APP_NAME)
					.withPath(path)
					.withLogged(logged)
					.withFatal(fatal)
					.build();
		}

		if (deleteCookies) {
			SecurityCookies.eatAllCookies(((ServletWebRequest) request).getRequest(),
					((ServletWebRequest) request).getResponse(), domain);
		}

		return ResponseDTO.builder()
				.apiError(error)
				.status(HttpStatus.UNAUTHORIZED)
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
				.status(status)
				.build();
	}
}
