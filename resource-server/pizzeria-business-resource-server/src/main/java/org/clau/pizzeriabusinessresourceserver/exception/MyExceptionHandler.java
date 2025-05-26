package org.clau.pizzeriabusinessresourceserver.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.service.ErrorService;
import org.clau.apiutils.util.ExceptionLogger;
import org.clau.apiutils.util.ServerUtils;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessresourceserver.util.Constant;
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

		String path = extractPath(request);

		String cause = body != null ? body.toString() : null;
		String message = "See cause";
		boolean fatal = false;

		APIError error = errorService.create(cause, message, Constant.APP_NAME, path, fatal);

		ResponseDTO response = ResponseDTO.builder().apiError(error).build();
		return new ResponseEntity<>(response, headers, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(Exception.class)
	protected ResponseEntity<ResponseDTO> unknownException(Exception ex, WebRequest request) {

		String path = extractPath(request);

		String cause = ex.getClass().getSimpleName();
		String message = ex.getMessage();
		boolean fatal = true;

		APIError error = errorService.create(cause, message, Constant.APP_NAME, path, fatal);
		ResponseDTO response = ResponseDTO.builder().apiError(error).build();

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
				.build();

		ExceptionLogger.log(ex, log, response);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
