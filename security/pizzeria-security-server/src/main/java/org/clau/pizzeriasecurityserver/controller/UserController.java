package org.clau.pizzeriasecurityserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.pizzeriadata.dto.common.ResponseDTO;
import org.clau.pizzeriadata.model.common.APIError;
import org.clau.pizzeriasecurityserver.controller.swagger.UserControllerSwagger;
import org.clau.pizzeriasecurityserver.service.UserService;
import org.clau.pizzeriautils.constant.ApiResponseMessages;
import org.clau.pizzeriautils.constant.ApiRoutes;
import org.clau.pizzeriautils.constant.MyApps;
import org.clau.pizzeriautils.util.TimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE)
public class UserController implements UserControllerSwagger {

   private final UserService userService;

   @DeleteMapping(ApiRoutes.USER_ID)
   public ResponseEntity<?> deleteById(@PathVariable Long userId, @RequestParam String password) {
	  try {
		 userService.deleteById(userId, password);
	  } catch (IllegalArgumentException ex) {
		 if (ApiResponseMessages.DUMMY_ACCOUNT_ERROR.equals(ex.getMessage())) {
			return ResponseEntity.badRequest().body(ResponseDTO.builder()
			   .apiError(APIError.builder()
				  .withId(UUID.randomUUID().getMostSignificantBits())
				  .withCreatedOn(TimeUtils.getNowAccountingDST())
				  .withCause(IllegalArgumentException.class.getSimpleName())
				  .withOrigin(MyApps.SECURITY_SERVER)
				  .withPath(ApiRoutes.API + ApiRoutes.V1 + ApiRoutes.USER_BASE + "/" + userId)
				  .withMessage(ApiResponseMessages.DUMMY_ACCOUNT_ERROR)
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

   @PostMapping(ApiRoutes.USER_ID + ApiRoutes.CHECK + "/password")
   public ResponseEntity<?> passwordMatches(@PathVariable Long userId, @RequestParam String password) {
	  userService.passwordMatches(userId, password);
	  return ResponseEntity.ok().build();
   }
}
