package com.greenlucky.utils;

import com.greenlucky.backend.persistence.domain.backend.User;

/**
 * Created by Mr Lam on 6/11/2016.
 */
public class UserUtils {

    /**
     * Non instantiable
     */
    private UserUtils(){ throw new AssertionError("Non instantiable");}

    /**
     * Creates a user basic attribute set
     * @return A user entity
     */
    public static User createBasicUser() {

        User user = new User();
        user.setUsername("basicUser");
        user.setPassword("secret");
        user.setEmail("basicUser@gmail.com");
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("0123456789");
        user.setCountry("VN");
        user.setEnabled(true);
        user.setDescription("A basic User");
        user.setProfileImageUrl("https://lh3.googleusercontent.com/Oh8BC2B14IcYmDCkfrME89EXgvg-zrfSpqugz5vpw2RjpmV_q7UPtSox-qgViE-fCXlLXHdxVg=w1920-h1080-rw-no");

        return user;

    }
}
