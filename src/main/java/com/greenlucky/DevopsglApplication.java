package com.greenlucky;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.greenlucky.backend.persistence.responsitories")
public class DevopsglApplication {
	public static void main(String[] args) {
		SpringApplication.run(DevopsglApplication.class, args);
	}
}
