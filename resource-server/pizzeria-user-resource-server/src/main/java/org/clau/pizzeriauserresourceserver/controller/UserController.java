package org.clau.pizzeriauserresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriauserresourceserver.controller.swagger.UserControllerSwagger;
import org.clau.pizzeriauserresourceserver.service.UserService;
import org.clau.pizzeriautils.constant.common.Response;
import org.clau.pizzeriautils.constant.common.Route;
import org.clau.pizzeriautils.dto.common.ResponseDTO;
import org.clau.pizzeriautils.util.common.TimeUtils;
import org.clau.pizzeriautils.util.common.constant.MyApps;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(Route.API + Route.V1 + Route.USER_BASE)
public class UserController implements UserControllerSwagger {

   private final UserService userService;

   @DeleteMapping(Route.USER_ID)
   public ResponseEntity<?> deleteById(@PathVariable Long userId, @RequestParam String password) {
	  try {
		 userService.deleteById(userId, password);
	  } catch (IllegalArgumentException ex) {
		 if (Response.DUMMY_ACCOUNT_ERROR.equals(ex.getMessage())) {
			return ResponseEntity.badRequest().body(ResponseDTO.builder()
			   .apiError(APIError.builder()
				  .withId(UUID.randomUUID().getMostSignificantBits())
				  .withCreatedOn(TimeUtils.getNowAccountingDST())
				  .withCause(IllegalArgumentException.class.getSimpleName())
				  .withOrigin(MyApps.RESOURCE_SERVER_USER)
				  .withPath(Route.API + Route.V1 + Route.USER_BASE + "/" + userId)
				  .withMessage(Response.DUMMY_ACCOUNT_ERROR)
				  .withLogged(false)
				  .withFatal(false)
				  .build())
			   .status(HttpStatus.BAD_REQUEST.value())
			   .build());
		 } else {
			throw ex;
		 }
	  }

	  return ResponseEntity.ok().build();
   }
}
