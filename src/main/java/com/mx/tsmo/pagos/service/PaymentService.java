package com.mx.tsmo.pagos.service;

import com.mx.tsmo.pagos.dto.PaymentIntentDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

public interface PaymentService {

    public PaymentIntent paymentIntent(PaymentIntentDto paymentIntentDto) throws StripeException;
    public PaymentIntent confirm(String id) throws StripeException;
    public PaymentIntent cancel(String id) throws StripeException;
}
