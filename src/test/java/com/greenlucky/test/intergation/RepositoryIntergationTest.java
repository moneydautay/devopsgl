package com.greenlucky.test.intergation;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.Role;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.persistence.responsitories.PlanRepository;
import com.greenlucky.backend.persistence.responsitories.RoleRepository;
import com.greenlucky.backend.persistence.responsitories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Mr Lam on 6/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class RepositoryIntergationTest {

    @Autowired
    private PlanRepository planRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    private static final int BASIC_PLAN_ID = 1;

    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(roleRepository);
    }


    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createBasicPlan();
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(BASIC_PLAN_ID);
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception{
        Role basicRole = createBasicRole();
        roleRepository.save(basicRole);
        Role retrievedRole = roleRepository.findOne(BASIC_PLAN_ID);
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void testCreateUser() throws Exception{

        Plan plan = createBasicPlan();
        planRepository.save(plan);

        User user = createBasicUser();
        user.setPlan(plan);

        Role basicRole = createBasicRole();
        Set<UserRole> userRoles = new HashSet<>();
        UserRole userRole = new UserRole(user, basicRole);
        userRoles.add(userRole);

        user.getUserRoles().addAll(userRoles);

        for (UserRole ur : userRoles) {
            roleRepository.save(ur.getRole());
        }

        user = userRepository.save(user);
        User newlyCreatedUser = userRepository.findOne(user.getId());
        Assert.assertNotNull(newlyCreatedUser);
        Assert.assertTrue(newlyCreatedUser.getId() != 0);
        Assert.assertNotNull(newlyCreatedUser.getPlan());
        Assert.assertNotNull(newlyCreatedUser.getPlan().getId());
        Set<UserRole> newlyCreatedUserUserRoles = newlyCreatedUser.getUserRoles();
        for (UserRole ur : newlyCreatedUserUserRoles) {
            Assert.assertNotNull(ur.getRole());
            Assert.assertNotNull(ur.getRole().getId());
        }



    }

    private Plan createBasicPlan() {
        Plan basicPlan = new Plan();
        basicPlan.setId(BASIC_PLAN_ID);
        basicPlan.setName("BASIC");
        return basicPlan;
    }

    private Role createBasicRole() {
        Role basicRole = new Role();
        basicRole.setId(BASIC_PLAN_ID);
        basicRole.setName("ROLE_USER");
        return basicRole;
    }

    private User createBasicUser() {
        User basicUser = new User();
        basicUser.setUsername("greenlucky");
        basicUser.setEmail("greenlucky@gmail.com");
        basicUser.setFirstName("Lam");
        basicUser.setLastName("Nguyen");
        basicUser.setPhoneNumber("01682153164");
        basicUser.setCountry("Viet Name");
        return basicUser;
    }
}
