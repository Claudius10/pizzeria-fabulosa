package org.clau.pizzeriabusinessclient.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.clau.pizzeriabusinessclient.util.Constant;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

	@GetMapping("/logged-in")
	public void user() {
		log.info("{} logged IN", Constant.APP_NAME);
	}
}
