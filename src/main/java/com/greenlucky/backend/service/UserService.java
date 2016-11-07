package com.greenlucky.backend.service;

import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
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

    @Transactional
    public User createUser(User user, PlansEnum plansEnum, Set<UserRole> userRoles){

        String encryptPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptPassword);

        Plan plan = new Plan(plansEnum);
        //It makes sure plansEnum exit in the database
        if(!planRepository.exists(plansEnum.getId())){
            plan = planRepository.save(plan);
        }

        user.setPlan(plan);

        for(UserRole userRole : userRoles){
            roleRepository.save(userRole.getRole());
        }

        user.getUserRoles().addAll(userRoles);

        user = userRepository.save(user);

        return user;
    }

    @Transactional
    public void updateUserPassword(long userId, String password){
        password = passwordEncoder.encode(password);
        userRepository.updateUserPassword(userId, password);
        LOGGER.debug("Password updated successfully for user id {}", userId);
    }
}
