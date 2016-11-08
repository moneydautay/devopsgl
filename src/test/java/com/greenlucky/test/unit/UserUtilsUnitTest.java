package com.greenlucky.test.unit;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.utils.UserUtils;
import com.greenlucky.web.controllers.ForgotMyPasswordController;
import com.greenlucky.web.domain.frontend.BasicAccountPayLoad;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.UUID;

/**
 * Created by Mr Lam on 7/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class UserUtilsUnitTest {

    private MockHttpServletRequest mockHttpServletRequest;

    private PodamFactory podamFactory;

    @Before
    public void init() throws Exception{
        mockHttpServletRequest = new MockHttpServletRequest();
        podamFactory = new PodamFactoryImpl();
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

    @Test
    public void mapWebUserToDomainUser() throws Exception{

        BasicAccountPayLoad webUser = podamFactory.manufacturePojoWithFullData(BasicAccountPayLoad.class);
        webUser.setEmail("nguyenlamit@gmail.com");

        User user = UserUtils.fromWebUserToDomainUser(webUser);
        Assert.assertNotNull(user);


        Assert.assertEquals(webUser.getUsername(), user.getUsername());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(webUser.getLastName(), user.getLastName());
        Assert.assertEquals(webUser.getEmail(), user.getEmail());
        Assert.assertEquals(webUser.getPassword(), user.getPassword());
        Assert.assertEquals(webUser.getDescription(), user.getDescription());
        Assert.assertEquals(webUser.getCountry(), user.getCountry());


    }

}
