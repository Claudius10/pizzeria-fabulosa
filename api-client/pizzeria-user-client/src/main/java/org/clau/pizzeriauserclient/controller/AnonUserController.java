package org.clau.pizzeriauserclient.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.clau.apiutils.constant.Route;
import org.clau.apiutils.dto.ResponseDTO;
import org.clau.pizzeriauserassets.dto.RegisterDTO;
import org.clau.pizzeriauserclient.controller.swagger.AnonUserControllerSwagger;
import org.clau.pizzeriauserclient.service.AnonUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping(Route.API + Route.V1 + Route.ANON_BASE)
public class AnonUserController implements AnonUserControllerSwagger {

	private final AnonUserService anonUserService;

	@PostMapping(Route.ANON_REGISTER)
	public Mono<ResponseEntity<Object>> registerAnonUser(@RequestBody @Valid RegisterDTO registerDTO) {

		Mono<ResponseEntity<Object>> result = anonUserService.createUser(registerDTO).map(response -> {

			if (response instanceof ResponseDTO responseDTO) {
				return ResponseEntity.status(responseDTO.getStatus()).body(response);
			} else {
				return ResponseEntity.status(HttpStatus.CREATED).build();
			}
		});

		return result;
	}
}
