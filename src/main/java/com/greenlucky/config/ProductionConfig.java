package com.greenlucky.config;

import com.greenlucky.backend.service.EmailService;
import com.greenlucky.backend.service.SmtpEmailService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Mr Lam on 4/11/2016.
 */
@Configuration
@Profile("prod")
@PropertySource("file:///${user.home}/Documents/Github/application-prod.properties")
public class ProductionConfig {

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }

}
