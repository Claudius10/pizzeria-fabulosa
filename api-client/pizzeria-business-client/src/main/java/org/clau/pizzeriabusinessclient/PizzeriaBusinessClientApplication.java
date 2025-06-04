package org.clau.pizzeriabusinessclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.apiutils.model"})
public class PizzeriaBusinessClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaBusinessClientApplication.class, args);
	}

}
