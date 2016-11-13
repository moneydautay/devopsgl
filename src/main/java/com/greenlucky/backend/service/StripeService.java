package com.greenlucky.backend.service;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Customer;
import com.stripe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Mr Lam on 13/11/2016.
 */
@Service
public class StripeService {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeService.class);

    @Autowired
    private String stripeKey;

    /**
     * Creates a Stripe customer and returns the Stripe customer id
     * @param tokenParams The credit card details to obtain a token. These will never be stored in the DB
     * @param customerParams The parameters which identify the customer
     * @return The stripe customer id which can then be used to perform billing operations at a later stage
     * @throws com.greenlucky.exceptions.StripeException If an error occurred while interacting with Stripe
     */
    public String createCustomer(Map<String, Object> tokenParams, Map<String, Object> customerParams) {

        Stripe.apiKey = stripeKey;

        String stripeCustomerId = null;
        try {
            Token token = Token.create(tokenParams);
            customerParams.put("source", token.getId());
            Customer customer = Customer.create(customerParams);
            stripeCustomerId = customer.getId();
        } catch (AuthenticationException e) {
            LOGGER.error("An authentication exception occurred while creating the Stripe customer", e);
            throw new com.greenlucky.exceptions.StripeException();
        } catch (InvalidRequestException e) {
            LOGGER.error("An invalid request exception occurred while creating the Stripe customer", e);
            throw new com.greenlucky.exceptions.StripeException();
        } catch (APIConnectionException e) {
            LOGGER.error("An API connection exception occurred while creating the Stripe customer", e);
            throw new com.greenlucky.exceptions.StripeException();
        } catch (CardException e) {
            LOGGER.error("A Credit Card exception occurred while creating the Stripe customer", e);
            throw new com.greenlucky.exceptions.StripeException();
        } catch (APIException e) {
            LOGGER.error("An API exception occurred while creating the Stripe customer", e);
            throw new com.greenlucky.exceptions.StripeException();
        }
        return stripeCustomerId;
    }
}
