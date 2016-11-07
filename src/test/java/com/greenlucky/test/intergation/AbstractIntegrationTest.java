package com.greenlucky.test.intergation;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.Role;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.persistence.responsitories.PlanRepository;
import com.greenlucky.backend.persistence.responsitories.RoleRepository;
import com.greenlucky.backend.persistence.responsitories.UserRepository;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.utils.UserUtils;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(DevopsglApplication.class)
public abstract class AbstractIntegrationTest {

    @Autowired
    protected PlanRepository planRepository;

    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected RoleRepository roleRepository;


    protected Plan createBasicPlan(PlansEnum plansEnum){
        return new Plan(plansEnum);
    }

    protected Role createBasicRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }

    protected User createBasicUser(String username, String email) {

        Plan plan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(plan);

        User user = UserUtils.createBasicUser(username, email);
        user.setPlan(plan);

        Role basicRole = createBasicRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);

        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(user, basicRole);
        userRoles.add(userRole);

        user.getUserRoles().addAll(userRoles);
        user = userRepository.save(user);
        return user;
    }

    public User createBasicUser(TestName testName){
        return createBasicUser(testName.getMethodName(), testName.getMethodName()+"@gmail.com");
    }

}