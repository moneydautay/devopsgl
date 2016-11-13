package com.greenlucky.test.intergation;

import com.greenlucky.DevopsglApplication;
import com.greenlucky.backend.service.StripeService;
import com.greenlucky.enums.PlansEnum;
import com.stripe.Stripe;
import com.stripe.model.Customer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Mr Lam on 13/11/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = DevopsglApplication.class)
public class StripeServiceIntegrateTest {

    /** The application logger */
    private static final Logger LOGGER = LoggerFactory.getLogger(StripeServiceIntegrateTest.class);

    public static final String TEST_CARD_NUMBER = "4242424242424242";

    public static final String TEST_CVC = "962";

    public static final String TEST_CARD_MONTH = "11";

    public static final String TEST_CARD_YEAR = "2018";


    @Autowired
    private StripeService stripeService;

    @Autowired
    private  String stripeKey;

    @Before
    public void init(){
        Assert.assertNotNull(stripeKey);
        Stripe.apiKey = stripeKey;
    }


    @Test
    public void retreiveCustomer() throws Exception{
        Stripe.apiKey = stripeKey;

        Customer customer = Customer.retrieve("cus_9YNjNW36wrNcUV");
        Assert.assertNotNull(customer);
    }

    @Test
    public void createStripeCustomer() throws Exception{

        Stripe.apiKey = stripeKey;

        LOGGER.info("The stripe key: "+ stripeKey);

        Map<String, Object> tokenParams = new HashMap<>();
        Map<String, Object> cardParams = new HashMap<>();

        cardParams.put("number", TEST_CARD_NUMBER);
        cardParams.put("exp_month", TEST_CARD_MONTH);
        cardParams.put("exp_year", TEST_CARD_YEAR);
        cardParams.put("cvc", TEST_CVC);
        tokenParams.put("card", cardParams);

        Map<String, Object> customerParams = new  HashMap<String, Object>();
        customerParams.put("description", "Customer for aubrey.thompson@example.com");
        customerParams.put("plan", PlansEnum.PRO.getId());

        LOGGER.info("Plan PRO ID: {}", PlansEnum.PRO.getId());

        String stripeCustomerId = stripeService.createCustomer(tokenParams, customerParams);
        Assert.assertNotNull(stripeCustomerId);

        Customer cu = Customer.retrieve(stripeCustomerId);
        cu.delete();

    }
}