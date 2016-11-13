package com.greenlucky.backend.service;

import com.greenlucky.backend.persistence.domain.backend.PasswordResetToken;
import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.persistence.responsitories.PasswordResetTokenRepository;
import com.greenlucky.backend.persistence.responsitories.PlanRepository;
import com.greenlucky.backend.persistence.responsitories.RoleRepository;
import com.greenlucky.backend.persistence.responsitories.UserRepository;
import com.greenlucky.enums.PlansEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * Created by Mr Lam on 6/11/2016.
 */
@Service
@Transactional(readOnly = true)
public class UserService{
    
    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){

        User localUser = userRepository.findByEmail(user.getEmail());

        if(localUser != null){
            LOGGER.error("User with username {} and email {} already email exist. Nothing will be done",
                    user.getUsername(),user.getEmail());
        }else {
            String encryptPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encryptPassword);

            Plan plan = new Plan(plansEnum);
            //It makes sure plansEnum exit in the database
            if (!planRepository.exists(plansEnum.getId())) {
                plan = planRepository.save(plan);
            }

            user.setPlan(plan);

            for (UserRole userRole : userRoles) {
                roleRepository.save(userRole.getRole());
            }

            user.getUserRoles().addAll(userRoles);

            localUser = userRepository.save(user);
        }

        return localUser;
    }

    /**
     * Returns a user given by email or null if user was not found.
     * @param email The email associated to the user to find
     * @return a user for the given email or null if user was not found
     */
    public User findByEmail(String email){
        return userRepository.findByEmail(email);
    }


    /**
     * Returns a user given by username or null if user was not found
     * @param username The username associated to the user to find
     * @return
     */
    public User findByUsername(String username){
        return userRepository.findByUsername(username);
    }

    /**
     * Update password given by userId and password
     * @param userId The user id given by required user to get new password (in forgot password) or change password
     * @param password The new password of user want changing
     */
    @Transactional
    public void updateUserPassword(long userId, String password){
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        LOGGER.debug("Password updated successfully for user id {}", userId);


        Set<PasswordResetToken> resetTokens = passwordResetTokenRepository.findAllByUserId(userId);
        if(!resetTokens.isEmpty())
            passwordResetTokenRepository.delete(resetTokens);
    }
}
