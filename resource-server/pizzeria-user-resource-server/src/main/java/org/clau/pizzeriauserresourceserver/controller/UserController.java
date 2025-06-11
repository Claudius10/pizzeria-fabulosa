package org.clau.pizzeriauserresourceserver.controller;

import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.pizzeriauserresourceserver.controller.swagger.UserControllerSwagger;
import org.clau.pizzeriauserresourceserver.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping(Route.API + Route.V1 + Route.USER_BASE)
public class UserController implements UserControllerSwagger {

	private final UserService userService;

	@DeleteMapping(Route.USER_ID)
	public ResponseEntity<?> deleteById(@PathVariable Long userId) {
		userService.deleteById(userId);
		return ResponseEntity.ok().build();
	}
}
