package org.clau.pizzeriaassetsclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PizzeriaAssetsClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaAssetsClientApplication.class, args);
	}

}
