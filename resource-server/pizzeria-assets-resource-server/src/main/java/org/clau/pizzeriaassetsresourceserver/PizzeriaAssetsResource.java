package org.clau.pizzeriaassetsresourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"org.clau.pizzeriastoreassets.model", "org.clau.pizzeriabusinessassets.model", "org.clau.apiutils.model"})
public class PizzeriaAssetsResource {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaAssetsResource.class, args);
	}
}
