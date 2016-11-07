package com.greenlucky.test.intergation;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.responsitories.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class UserRepositoryIntegratationTest extends AbstractIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Rule public TestName testName = new TestName();

    @Before
    public void init() throws Exception{
        Assert.assertNotNull(userRepository);
    }

    @Test
    public void testFindUserByEmail() throws Exception{
        String email = testName.getMethodName()+"@gmail.com";
        createBasicUser(testName);

        User user = userRepository.findByEmail(email);
        Assert.assertNotNull(user);
    }
}