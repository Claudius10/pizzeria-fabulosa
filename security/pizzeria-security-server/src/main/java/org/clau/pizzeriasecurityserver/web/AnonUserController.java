package org.clau.pizzeriasecurityserver.web;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Response;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.apiutils.model.APIError;
import org.clau.apiutils.util.TimeUtils;
import org.clau.pizzeriasecurityserver.service.AnonUserService;
import org.clau.pizzeriasecurityserver.util.Constant;
import org.clau.pizzeriasecurityserver.web.swagger.AnonUserControllerSwagger;
import org.clau.pizzeriauserassets.dto.RegisterDTO;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(Route.API + Route.V1 + Route.PUBLIC + Route.USER_BASE)
public class AnonUserController implements AnonUserControllerSwagger {

   private final AnonUserService anonUserService;

   @PostMapping(Route.REGISTER)
   public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
	  try {
		 anonUserService.create(registerDTO);
	  } catch (DataIntegrityViolationException ex) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ex.getClass().getSimpleName())
			   .withOrigin(Constant.APP_NAME)
			   .withPath(request.getPathInfo())
			   .withMessage(Response.USER_EMAIL_ALREADY_EXISTS)
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }

	  return ResponseEntity.status(HttpStatus.CREATED).build();
   }
}
