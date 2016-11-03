package com;

import com.greenlucky.DevopsglApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
@SpringBootTest
public class DevopsglApplicationTests {

	/** The application logger */
	private static final Logger LOGGER = LoggerFactory.getLogger(DevopsglApplicationTests.class);


	@Test
	public void contextLoads() {
		LOGGER.debug("Message test Logger..");
	}

}
