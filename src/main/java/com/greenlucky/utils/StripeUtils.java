package com.greenlucky.utils;

import com.greenlucky.web.domain.frontend.ProAccountPayload;

import java.util.Map;
import java.util.HashMap;

/**
 * Created by Mr Lam on 13/11/2016.
 */
public class StripeUtils {

    public static final String STRIPE_CARD_NUMBER = "number";
    public static final String STRIPE_CARD_MONTH = "exp_month";
    public static final String STRIPE_CARD_YEAR = "exp_year";
    public static final String STRIPE_CARD_KEY = "card";
    public static final String STRIPE_CVC = "cvc";

    private StripeUtils(){ throw  new AssertionError("Non instantiable");}

    /**
     * Given the card info from by the user in the front-end, which return the parameters to map to obtain a Stripe token.
     * @param payload The info provided by the user during registration
     * @return A parameter map that can be user to obtain a Stripe token
     */
    public static Map<String, Object> extractTokenParamsFromSignupPayload(ProAccountPayload payload){
        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();

        cardParams.put(STRIPE_CARD_NUMBER,payload.getCardNumber());
        cardParams.put(STRIPE_CARD_MONTH,payload.getCardMonth());
        cardParams.put(STRIPE_CARD_YEAR,payload.getCardYear());
        cardParams.put(STRIPE_CVC,payload.getCardCode());
        tokenParams.put(STRIPE_CARD_KEY,cardParams);
        return tokenParams;
    }
}
