package com.greenlucky.config;

import com.greenlucky.backend.service.EmailService;
import com.greenlucky.backend.service.SmtpEmailService;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${stripe.prod.private.key}")
    private String stripeProKey;

    @Bean
    public EmailService emailService(){
        return new SmtpEmailService();
    }

    @Bean
    public String stripeKey(){
        return stripeProKey;
    }

}
