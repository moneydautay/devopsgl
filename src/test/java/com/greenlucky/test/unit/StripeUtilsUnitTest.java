package com.greenlucky.test.unit;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.test.intergation.StripeServiceIntegrateTest;
import com.greenlucky.utils.StripeUtils;
import com.greenlucky.web.domain.frontend.ProAccountPayload;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

/**
 * Created by Mr Lam on 13/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class StripeUtilsUnitTest {

    @Test
    public void createStripeTokenParamsFromUserPayLoad(){

        ProAccountPayload payload = new ProAccountPayload();
        String cardNumber = StripeServiceIntegrateTest.TEST_CARD_NUMBER;
        payload.setCardNumber(cardNumber);
        String cardMonth = StripeServiceIntegrateTest.TEST_CARD_MONTH;
        payload.setCardMonth(cardMonth);
        String cardYear = StripeServiceIntegrateTest.TEST_CARD_YEAR;
        payload.setCardYear(cardYear);
        String cvc = StripeServiceIntegrateTest.TEST_CVC;
        payload.setCardCode(cvc);

        Map<String, Object> tokenParams = StripeUtils.extractTokenParamsFromSignupPayload(payload);
        Map<String, Object> cardParams = (Map<String, Object>) tokenParams.get(StripeUtils.STRIPE_CARD_KEY);

        Assert.assertEquals(cardNumber, cardParams.get(StripeUtils.STRIPE_CARD_NUMBER));
        Assert.assertEquals(cardMonth, cardParams.get(StripeUtils.STRIPE_CARD_MONTH));
        Assert.assertEquals(cardYear, cardParams.get(StripeUtils.STRIPE_CARD_YEAR));
        Assert.assertEquals(cvc, cardParams.get(StripeUtils.STRIPE_CVC));
    }


}