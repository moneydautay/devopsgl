package com.greenlucky.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created by Mr Lam on 6/11/2016.
 */
@Configuration
@EnableJpaRepositories(basePackages = "com.greenlucky.backend.persistence.responsitories")
@EntityScan(basePackages = "com.greenlucky.backend.persistence.domain.backend")
@EnableTransactionManagement
@PropertySource("file:///${user.home}/Documents/Github/application-common.properties")
public class ApplicationConfig {
}
