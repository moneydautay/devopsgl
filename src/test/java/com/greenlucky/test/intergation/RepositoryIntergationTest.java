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


    @Before
    public void init(){
        Assert.assertNotNull(planRepository);
        Assert.assertNotNull(userRepository);
        Assert.assertNotNull(roleRepository);
    }


    @Test
    public void testCreateNewPlan() throws Exception{
        Plan basicPlan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(basicPlan);
        Plan retrievedPlan = planRepository.findOne(PlansEnum.BASIC.getId());
        Assert.assertNotNull(retrievedPlan);
    }

    @Test
    public void testCreateNewRole() throws Exception{
        Role basicRole = createBasicRole(RolesEnum.BASIC);
        roleRepository.save(basicRole);
        Role retrievedRole = roleRepository.findOne(RolesEnum.BASIC.getId());
        Assert.assertNotNull(retrievedRole);
    }

    @Test
    public void testCreateUser() throws Exception{


        User basicUser = createBasicUser();

        User newlyCreatedUser = userRepository.findOne(basicUser.getId());
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

    @Test
    public void testDeleteUser() throws Exception{
        User basicUser = createBasicUser();
        userRepository.delete(basicUser.getId());
    }

    private User createBasicUser() {

        Plan plan = createBasicPlan(PlansEnum.BASIC);
        planRepository.save(plan);

        User user = UserUtils.createBasicUser();
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

    private Plan createBasicPlan(PlansEnum plansEnum){
        return new Plan(plansEnum);
    }

    private Role createBasicRole(RolesEnum rolesEnum) {
        return new Role(rolesEnum);
    }
}
