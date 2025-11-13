package org.clau.pizzeriasecurityserver.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriasecurityserver.controller.swagger.RegisterControllerSwagger;
import org.clau.pizzeriasecurityserver.data.dto.RegisterDTO;
import org.clau.pizzeriasecurityserver.service.RegisterService;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.util.TimeUtils;
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
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.REGISTER)
public class RegisterController implements RegisterControllerSwagger {

   private final RegisterService registerService;

   @PostMapping(ApiRoutes.USER_BASE)
   public ResponseEntity<?> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO, HttpServletRequest request) {
	  try {
		 registerService.create(registerDTO);
	  } catch (DataIntegrityViolationException ex) {
		 return ResponseEntity.badRequest().body(ResponseDTO.builder()
			.apiError(APIError.builder()
			   .withId(UUID.randomUUID().getMostSignificantBits())
			   .withCreatedOn(TimeUtils.getNowAccountingDST())
			   .withCause(ex.getClass().getSimpleName())
			   .withOrigin(MyApps.SECURITY_SERVER)
			   .withPath(request.getPathInfo())
			   .withMessage(ApiResponseMessages.USER_EMAIL_ALREADY_EXISTS)
			   .withLogged(false)
			   .withFatal(false)
			   .build())
			.status(HttpStatus.BAD_REQUEST.value())
			.build());
	  }

	  return ResponseEntity.status(HttpStatus.CREATED).build();
   }
}
