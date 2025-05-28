package org.clau.pizzeriabusinessresourceserver.config;

import com.fasterxml.jackson.module.mrbean.MrBeanModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class JacksonConfig {

	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		return builder -> builder.modulesToInstall(new MrBeanModule());
	}
}
