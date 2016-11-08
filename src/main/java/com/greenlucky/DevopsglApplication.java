package com.greenlucky;

import com.greenlucky.backend.persistence.domain.backend.Role;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.service.PlanService;
import com.greenlucky.backend.service.UserService;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.utils.UserUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Autowired
	private PlanService planService;

	@Value("${webmaster.username}")
	private String webmasterUsername;

	@Value("${webmaster.password}")
	private String webmasterPassword;

	@Value("${webmaster.email}")
	private String webmasterEmail;

	public static void main(String[] args) {
		SpringApplication.run(DevopsglApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		LOGGER.info("Creating Basic and Pro plans into the database..");
		//Creates a basic plan
		planService.createPlan(PlansEnum.BASIC.getId());
		//Creates a pro plan
		planService.createPlan(PlansEnum.PRO.getId());

		User user = UserUtils.createBasicUser(webmasterUsername, webmasterEmail);
		user.setPassword(webmasterPassword);

		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user, new Role(RolesEnum.ADMIN)));
		LOGGER.debug("Creating user with username: {}", user.getUsername());
		userService.createUser(user, PlansEnum.PRO, userRoles);
		LOGGER.info("User {} created", user.getUsername());
	}
}
