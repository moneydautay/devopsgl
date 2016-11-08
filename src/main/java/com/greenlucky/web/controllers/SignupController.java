package com.greenlucky.web.controllers;

import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.service.I18NService;
import com.greenlucky.backend.service.PlanService;
import com.greenlucky.backend.service.UserService;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.utils.UserUtils;
import com.greenlucky.web.domain.frontend.ProAccountPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

/**
 * Created by Mr Lam on 8/11/2016.
 */
@Controller
public class SignupController {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SignupController.class);


    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;


    @Autowired
    private I18NService i18NService;

    public static final String SIGNUP_URL_MAPPING = "/signup";

    public static final String PAYLOAD_MODEL_KEY_NAME = "payload";

    public static final String SUBSCRIPTION_VIEW_NAME = "registration/signup";

    private static final String DUPLICATED_USERNAME_KEY = "duplicatedUsername";

    private static final String DUPLICATED_EMAIL_KEY = "duplicatedEmail";

    private static final String SIGNED_UP_MESSAGE_KEY = "signedUp";

    private static final String ERROR_MESSAGE_KEY = "message";

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.GET)
    public String signupGet(@RequestParam("planId") int planId, Model model){

        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId())
            throw new IllegalArgumentException("Plan id is not valid");

        model.addAttribute(PAYLOAD_MODEL_KEY_NAME, new ProAccountPayload());

        return SUBSCRIPTION_VIEW_NAME;
    }

    @RequestMapping(value = SIGNUP_URL_MAPPING, method = RequestMethod.POST)
    public String signupPost(@RequestParam("planId") int planId,
                             @ModelAttribute(PAYLOAD_MODEL_KEY_NAME) @Valid ProAccountPayload payload,
                             Locale locale, Model model) throws IOException{
        if(planId != PlansEnum.BASIC.getId() && planId != PlansEnum.PRO.getId()){
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY,"Plan id is not exists");
            return SUBSCRIPTION_VIEW_NAME;
        }

        this.checkForDuplicates(payload, model);

        boolean duplicates = false;

        List<String> errorMessages = new ArrayList<>();

        if(model.containsAttribute(DUPLICATED_USERNAME_KEY)){
            String existedUsername = i18NService.getMessage("signup.form.error.username.already.exists", locale);
            LOGGER.error(existedUsername);
            errorMessages.add(existedUsername);
            duplicates = true;
        }

        if(model.containsAttribute(DUPLICATED_EMAIL_KEY)){
            String existedEmail = i18NService.getMessage("signup.form.error.email.already.exists", locale);
            LOGGER.error(existedEmail);
            errorMessages.add(existedEmail);
            duplicates = true;
        }

        if(duplicates){
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, errorMessages);
            return SUBSCRIPTION_VIEW_NAME;
        }

        LOGGER.debug("Transforming user payload into User domain object");
        User user = UserUtils.fromWebUserToDomainUser(payload);

        //Set the plan and role defend on user's choosing plan
        Plan choosedPlan = planService.findByPlanId(planId);

        if(choosedPlan == null){
            LOGGER.error("The plan {} was not found....", planId);
            model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
            model.addAttribute(ERROR_MESSAGE_KEY, i18NService.getMessage("signup.form.error.plan.not.found",locale));
            return SUBSCRIPTION_VIEW_NAME;
        }

        user.setPlan(choosedPlan);

        User registeredUser = null;

        Set<UserRole> roles = new HashSet<>();

        if(planId == PlansEnum.BASIC.getId()){
            roles.add(new UserRole(user, RolesEnum.BASIC));
            registeredUser = userService.createUser(user, PlansEnum.BASIC, roles);
            LOGGER.debug("Add role {} for user {}", roles, user);
        }else{
            roles.add(new UserRole(user, RolesEnum.PRO));
            registeredUser = userService.createUser(user, PlansEnum.PRO, roles);
            LOGGER.debug("Add role {} for user {}", roles, user);
        }

        //Auto login the registered user
        Authentication auth = new UsernamePasswordAuthenticationToken(registeredUser, null, registeredUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);

        LOGGER.info("User {} has been created and logged to application ", registeredUser);

        //returns message true
        model.addAttribute(SIGNED_UP_MESSAGE_KEY, "true");

        return SUBSCRIPTION_VIEW_NAME;
    }

    /**
     * This function will check existed username and email in database if exist to set DUPLICATED_USERNAME_KEY is true or DUPLICATED_EMAIL_KEY is true
     * @param payload The payload given
     * @param model The model given
     */
    private void checkForDuplicates(ProAccountPayload payload, Model model) {

        if(userService.findByUsername(payload.getUsername()) != null)
            model.addAttribute(DUPLICATED_USERNAME_KEY, true);

        if(userService.findByEmail(payload.getEmail()) != null)
            model.addAttribute(DUPLICATED_EMAIL_KEY, true);
    }
}
