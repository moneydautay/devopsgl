package com.greenlucky.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * Created by Mr Lam on 3/11/2016.
 */
@Configuration
public class I18NConfig {

    @Bean
    public ReloadableResourceBundleMessageSource messageSource(){
        ReloadableResourceBundleMessageSource resourceBundleMessageSource = new ReloadableResourceBundleMessageSource();
        resourceBundleMessageSource.setBasename("classpath:i18n/messages");
        //Check for new message every 30 minutes
        resourceBundleMessageSource.setCacheMillis(1800);
        return  resourceBundleMessageSource;
    }
}
