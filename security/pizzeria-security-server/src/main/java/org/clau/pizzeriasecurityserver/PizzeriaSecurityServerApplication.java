package org.clau.pizzeriasecurityserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class PizzeriaSecurityServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(PizzeriaSecurityServerApplication.class, args);
	}

}
