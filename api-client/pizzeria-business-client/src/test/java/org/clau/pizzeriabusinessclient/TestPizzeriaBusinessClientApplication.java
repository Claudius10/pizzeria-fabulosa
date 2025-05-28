package org.clau.pizzeriabusinessclient;

import org.springframework.boot.SpringApplication;

public class TestPizzeriaBusinessClientApplication {

	public static void main(String[] args) {
		SpringApplication.from(PizzeriaBusinessClientApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
