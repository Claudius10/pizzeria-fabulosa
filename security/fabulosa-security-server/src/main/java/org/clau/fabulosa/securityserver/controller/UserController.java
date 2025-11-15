package org.clau.fabulosa.securityserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.fabulosa.data.dto.common.ResponseDTO;
import org.clau.fabulosa.data.model.common.APIError;
import org.clau.fabulosa.securityserver.controller.swagger.UserControllerSwagger;
import org.clau.fabulosa.securityserver.service.user.UserService;
import org.clau.fabulosa.utils.constant.ApiResponseMessages;
import org.clau.fabulosa.utils.constant.ApiRoutes;
import org.clau.fabulosa.utils.constant.MyApps;
import org.clau.fabulosa.utils.util.TimeUtils;
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
