package com.greenlucky.backend.service;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToken;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.responsitories.PasswordResetTokenRepository;
import com.greenlucky.backend.persistence.responsitories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@Service
@Transactional(readOnly = true)
public class PasswordResetTokenService{

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Value("${token.expiration.length.minutes}")
    private int tokenExpirationMinutes;

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordResetTokenService.class);

    /**
     * Retrieves a password token given by a token
     * @param token The token to be returned
     * @return PasswordResetToken if was found one else null if was not found
     */
    public PasswordResetToken findByToken(String token) {
        return passwordResetTokenRepository.findByToken(token);
    }


    /**
     * Creates a new password reset token for a user identified by the given email.
     * @param email the email unique identifying the user
     * @return a new Password reset token for the user identified by the given email or null if was not found
     */
    @Transactional
    public PasswordResetToken createPasswordResetTokenForEmail(String email){

        PasswordResetToken passwordResetToken = null;

        User user = userRepository.findByEmail(email);

        if(null != user){
            String token = UUID.randomUUID().toString();
            LocalDateTime now = LocalDateTime.now(Clock.systemUTC());
            passwordResetToken = new PasswordResetToken(token, user, now, tokenExpirationMinutes);

            passwordResetToken = passwordResetTokenRepository.save(passwordResetToken);

            LOGGER.debug("Successfully created token {} for user {}", token, user.getUsername());
        }else{
            LOGGER.warn("We couldn't find a user for the given email {}", email);
        }
        return passwordResetToken;
    }


}
