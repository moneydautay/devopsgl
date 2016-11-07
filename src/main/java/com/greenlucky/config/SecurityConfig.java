package com.greenlucky.config;

import com.greenlucky.backend.service.UserServiceSecurity;
import com.greenlucky.web.controllers.ForgotMyPasswordController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Mr Lam on 5/11/2016.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter{

    /** The key enscryption password */
    private static final String SALT = "asdkfjjew233240**(#kl43324";

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(12, new SecureRandom(SALT.getBytes()));
    }



    @Autowired
    private UserServiceSecurity userServiceSecurity;

    @Autowired
    private Environment env;

    /** Public URLs */
    private static final String[] PUBLIC_MATCHES= {
            "/webjars/**",
            "/css/**",
            "/js/**",
            "/images/**",
            "/",
            "/about/**",
            "/contact/**",
            "/error/**",
            "/console/**",
            ForgotMyPasswordController.FORGOT_PASSWORD_URL_MAPPING
    };

    @Override
    protected  void configure(HttpSecurity http) throws Exception{

        List<String> activeProfiles = Arrays.asList(env.getActiveProfiles());
        if(activeProfiles.contains("dev")){
            http.csrf().disable();
            http.headers().frameOptions().disable();
        }

        http
                .authorizeRequests()
                .antMatchers(PUBLIC_MATCHES).permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/login").defaultSuccessUrl("/payload")
                .failureUrl("/login?error=true").permitAll()
                .and()
                .logout().permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
               .userDetailsService(userServiceSecurity)
                .passwordEncoder(passwordEncoder());
    }
}
