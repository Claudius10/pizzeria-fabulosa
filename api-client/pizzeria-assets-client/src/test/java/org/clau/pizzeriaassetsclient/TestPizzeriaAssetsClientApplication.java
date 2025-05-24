package org.clau.pizzeriaassetsclient;

import org.springframework.boot.SpringApplication;

public class TestPizzeriaAssetsClientApplication {

	public static void main(String[] args) {
		SpringApplication.from(PizzeriaAssetsClientApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
