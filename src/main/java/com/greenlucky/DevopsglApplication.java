package com.greenlucky;

import com.greenlucky.backend.persistence.domain.backend.Role;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.service.UserService;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class DevopsglApplication implements CommandLineRunner{

	/** The application logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(DevopsglApplication.class);

	@Autowired
	private UserService userService;

	public static void main(String[] args) {
		SpringApplication.run(DevopsglApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		String username = "userpro";
		String email = "userpro@gmail.com";
		User user = UserUtils.createBasicUser(username, email);
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, new Role(RolesEnum.BASIC)));
		LOGGER.debug("Creating user with username: {}", user.getUsername());
		userService.createUser(user, PlansEnum.BASIC, userRoles);
		LOGGER.info("User {} created", user.getUsername());
	}
}
