package org.clau.pizzeriabusinessclient.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriabusinessclient.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
public class AuthorizationController {

	@GetMapping("/authorized")
	public ResponseEntity<?> authorized() {
		// after consent has been granted, i.e. scopes have been authorized, this endpoint is called
		return ResponseEntity.ok("authorized");
	}

	@GetMapping(value = "/authorized", params = OAuth2ParameterNames.ERROR)
	public ResponseEntity<?> authorizationFailed(HttpServletRequest request) {
		String errorCode = request.getParameter(OAuth2ParameterNames.ERROR);

		if (StringUtils.hasText(errorCode)) {

			ResponseDTO response = ResponseDTO.builder()
					.apiError(APIError.builder()
							.withId(UUID.randomUUID().getMostSignificantBits())
							.withCreatedOn(TimeUtils.getNowAccountingDST())
							.withOrigin(Constant.APP_NAME)
							.withCause("OAuth2-AuthorizationFailed")
							.withMessage(OAuth2ParameterNames.ERROR_DESCRIPTION)
							.withPath(OAuth2ParameterNames.ERROR_URI)
							.withLogged(false)
							.withFatal(false)
							.build())
					.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.build();

			return ResponseEntity.internalServerError().body(response);
		}

		return ResponseEntity.ok().build();
	}
}
