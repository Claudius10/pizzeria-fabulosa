package org.clau.pizzeriabusinessclient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PizzeriaBusinessClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaBusinessClientApplication.class, args);
	}

}
