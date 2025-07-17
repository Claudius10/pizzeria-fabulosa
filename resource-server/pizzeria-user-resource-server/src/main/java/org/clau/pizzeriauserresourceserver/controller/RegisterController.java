package org.clau.pizzeriauserresourceserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriauserresourceserver.controller.swagger.RegisterControllerSwagger;
import org.clau.pizzeriauserresourceserver.service.AnonUserService;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.dto.user.RegisterDTO;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.util.common.constant.MyApps;
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
@RequestMapping(Route.API + Route.V1 + Route.REGISTER)
public class RegisterController implements RegisterControllerSwagger {

   private final AnonUserService anonUserService;

   @PostMapping(Route.USER_BASE)
   public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
	  try {
		 anonUserService.create(registerDTO);
	  } catch (DataIntegrityViolationException ex) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ex.getClass().getSimpleName())
			   .withOrigin(MyApps.RESOURCE_SERVER_USER)
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
