package com.greenlucky.utils;

import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.web.controllers.ForgotMyPasswordController;
import com.greenlucky.web.domain.frontend.BasicAccountPayLoad;

import javax.servlet.http.HttpServletRequest;

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
    public static User createBasicUser(String username, String email) {

        User user = new User();
        user.setUsername(username);
        user.setPassword("secret");
        user.setEmail(email);
        user.setFirstName("firstName");
        user.setLastName("lastName");
        user.setPhoneNumber("0123456789");
        user.setCountry("VN");
        user.setEnabled(true);
        user.setDescription("A basic User");
        user.setProfileImageUrl("https://lh3.googleusercontent.com/Oh8BC2B14IcYmDCkfrME89EXgvg-zrfSpqugz5vpw2RjpmV_q7UPtSox-qgViE-fCXlLXHdxVg=w1920-h1080-rw-no");

        return user;
    }


    /**
     * Creates create password reset url from HttpServletRequest, userId and token
     * @param request
     * @param userId
     * @param token
     * @return password reset url from HttpServletRequest, userId and token
     */
    public static String createPasswordResetUrl(HttpServletRequest request, Long userId, String token) {
        String passwordResetUrl =
                request.getScheme() +
                        "://" +
                        request.getServerName() +
                        ":" +
                        request.getServerPort() +
                        request.getContextPath() +
                        ForgotMyPasswordController.CHANGE_PASSWORD_PATH +
                        "?id="+
                        userId +
                        "&token=" +
                        token;
        return passwordResetUrl;
    }

    public static <T extends BasicAccountPayLoad> User fromWebUserToDomainUser(T frontendPayload) {
        User user = new User();
        user.setUsername(frontendPayload.getUsername());
        user.setPassword(frontendPayload.getPassword());
        user.setFirstName(frontendPayload.getFirstName());
        user.setLastName(frontendPayload.getLastName());
        user.setEmail(frontendPayload.getEmail());
        user.setPhoneNumber(frontendPayload.getPhoneNumber());
        user.setDescription(frontendPayload.getDescription());
        user.setEnabled(true);
        user.setCountry(frontendPayload.getCountry());

        return user;
    }
}
