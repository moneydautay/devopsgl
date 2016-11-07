package com.greenlucky.test.intergation;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.persistence.domain.backend.Role;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.service.UserService;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.utils.UserUtils;
import org.junit.Assert;
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
public abstract class AbstracServiceIntegrationTest {

    @Autowired
    private UserService userService;

    protected User createUser(TestName testName){
        String username = testName.getMethodName();
        String email = username+"@gmail.com";

        Set<UserRole> userRoles = new HashSet<>();
        User userBasic = UserUtils.createBasicUser(username, email);
        userRoles.add(new UserRole(userBasic,new Role(RolesEnum.BASIC)));

        User user = userService.createUser(userBasic, PlansEnum.BASIC, userRoles);
        Assert.assertNotNull(user);
        Assert.assertNotNull(user.getId());
        return user;
    }
}
