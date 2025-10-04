package com.ayanda.HealthEaseApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class HealthEaseApiApplication  extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(HealthEaseApiApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(HealthEaseApiApplication.class, args);
	}

}
