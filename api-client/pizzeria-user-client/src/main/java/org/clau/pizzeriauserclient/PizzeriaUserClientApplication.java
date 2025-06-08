package org.clau.pizzeriauserclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.apiutils.model"})
public class PizzeriaUserClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaUserClientApplication.class, args);
	}

}
