package com.greenlucky.web.controllers;

import com.greenlucky.backend.persistence.domain.backend.Plan;
import com.greenlucky.backend.persistence.domain.backend.User;
import com.greenlucky.backend.persistence.domain.backend.UserRole;
import com.greenlucky.backend.service.*;
import com.greenlucky.enums.PlansEnum;
import com.greenlucky.enums.RolesEnum;
import com.greenlucky.exceptions.S3Exception;
import com.greenlucky.exceptions.StripeException;
import com.greenlucky.utils.StripeUtils;
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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Mr Lam on 8/11/2016.
 */
@Controller
public class SignupController {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(SignupController.class);
    private static final String GENERIC_ERROR_VIEW_NAME = "/error/genericError";


    @Autowired
    private UserService userService;

    @Autowired
    private PlanService planService;


    @Autowired
    private I18NService i18NService;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private StripeService stripeService;

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
    public String signupPost(@RequestParam(name = "planId", required = true) int planId,
                             @RequestParam(name = "profile", required = false) MultipartFile file,
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

        LOGGER.error("File name: "+file.getOriginalFilename());
        //Check profile file not null
        if(file != null && !file.isEmpty()){
            String profileImageUrl = s3Service.storeProfileImage(file, payload.getUsername());
            if(profileImageUrl != null){
                user.setProfileImageUrl(profileImageUrl);
            }else{
                LOGGER.error("There are a problem uploading the profile image to S3 server. "
                        +"The user's profile will be created without the image profile");
            }

        }

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

            if(StringUtils.isEmpty(payload.getCardCode()) ||
                    StringUtils.isEmpty(payload.getCardMonth()) ||
                    StringUtils.isEmpty(payload.getCardYear()) ||
                    StringUtils.isEmpty(payload.getCardNumber())){
                LOGGER.error("One or more credit card filds is empty  or null. Return error to the user");
                model.addAttribute(SIGNED_UP_MESSAGE_KEY, "false");
                model.addAttribute(ERROR_MESSAGE_KEY, i18NService.getMessage("signup.form.error.credit.card", locale));
                return SUBSCRIPTION_VIEW_NAME;
            }

            //If the user select the PRO account, creates the stripe customer to store the stripe customer id in the stripe
            Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);

            Map<String, Object> customerParams = new HashMap<>();
            customerParams.put("description","Devoplsgl customer. Username: "+payload.getUsername());
            customerParams.put("email", payload.getEmail());
            customerParams.put("plan", choosedPlan.getId());

            LOGGER.info("Subscribing the customer to plan {}", choosedPlan.getName());

            String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);

            LOGGER.info("Username: {} has been subscribed to Stripe", payload.getUsername());

            user.setStripeCustomerId(stripeCustomerId);

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


    @ExceptionHandler({StripeException.class, S3Exception.class})
    public ModelAndView sigupException(HttpServletRequest request, Exception ex){
        LOGGER.error("Request {} raised exception {}", request.getRequestURL(), ex);

        ModelAndView model = new ModelAndView();
        model.addObject("exception", ex);
        model.addObject("url", request.getRequestURL());
        model.addObject("timestamp", LocalDate.now(Clock.systemUTC()));
        model.setViewName(GENERIC_ERROR_VIEW_NAME);
        return model;
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
