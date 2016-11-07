package com.greenlucky.test.unit;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.utils.UserUtils;
import com.greenlucky.web.controllers.ForgotMyPasswordController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.UUID;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    @Before
    public void init() throws Exception{
        mockHttpServletRequest = new MockHttpServletRequest();
    }

    @Test
    public  void testPasswordResetUserEmailUrlContruction() throws  Exception{

        mockHttpServletRequest.setServerPort(8080);

        String token = UUID.randomUUID().toString();

        long userId = 123456;

        String expectUlr = "http://localhost:8080" +
                ForgotMyPasswordController.CHANGE_PASSWORD_PATH + "?id="+ userId +"&token="+token;

        String actualUrl = UserUtils.createPasswordResetUrl(mockHttpServletRequest, userId, token);

        Assert.assertEquals(expectUlr, actualUrl);
    }
}
