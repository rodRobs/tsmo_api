package com.mx.tsmo.pagos.service;

import com.mx.tsmo.pagos.dto.PaymentIntentDto;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.key.secret}")
    String secretKey;

    @Override
    public PaymentIntent paymentIntent(PaymentIntentDto paymentIntentDto) throws StripeException {
        Stripe.apiKey = secretKey;

        List<Object> paymentMethodTypes = new ArrayList<>();
        paymentMethodTypes.add("card");
        Map<String, Object> params = new HashMap<>();
        params.put("amount", paymentIntentDto.getAmount());
        params.put("currency", paymentIntentDto.getCurrency());
        params.put("description", paymentIntentDto.getDescription());
        //params.put("number", paymentIntentDto.)
        params.put("payment_method_types", paymentMethodTypes);
        return PaymentIntent.create(params);
    }

    @Override
    public PaymentIntent confirm(String id) {
        Stripe.apiKey = secretKey;
        try {
            PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
            Map<String, Object> params = new HashMap<>();
            params.put("payment_method", "pm_card_visa");
            // params.put("type", "card");

            paymentIntent.confirm(params);
            return paymentIntent;
        } catch (StripeException se) {
            log.error("ERROR: Stripe Exception: "+se.getMessage());
            se.getStackTrace();
            return null;
        }
    }

    @Override
    public PaymentIntent cancel(String id) throws StripeException {
        Stripe.apiKey = secretKey;
        PaymentIntent paymentIntent = PaymentIntent.retrieve(id);
        paymentIntent.cancel();
        return paymentIntent;
    }


}
