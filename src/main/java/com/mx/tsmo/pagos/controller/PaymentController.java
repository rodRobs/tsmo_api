package com.mx.tsmo.pagos.controller;

import com.mx.tsmo.pagos.dto.PaymentIntentDto;
import com.mx.tsmo.pagos.service.PaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stripe")
@CrossOrigin("*")
@Slf4j
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/paymentIntent")
    public ResponseEntity<String> payment(@RequestBody PaymentIntentDto paymentIntentDto) throws StripeException {
        log.info("Entra a servicio de payment");
        PaymentIntent paymentIntent = paymentService.paymentIntent(paymentIntentDto);
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<String> confirm(@PathVariable("id") String id) {
        log.info("Entra a servicio para confirm, id: "+id);
        try {
            PaymentIntent paymentIntent = paymentService.confirm(id);
            String paymentStr = paymentIntent.toJson();
            return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
        } catch (StripeException se) {
            log.error(se.getMessage());
            se.printStackTrace();
            return new ResponseEntity<String>("No se pudo concretar el pago", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancel(@PathVariable("id") String id) throws StripeException {
        log.info("Entra a servicio para cancel");
        PaymentIntent paymentIntent = paymentService.cancel(id);
        if (paymentIntent == null) {
            log.error("Hubo error");
            return new ResponseEntity<String>("No se pudo concretar", HttpStatus.BAD_REQUEST);
        }
        String paymentStr = paymentIntent.toJson();
        return new ResponseEntity<String>(paymentStr, HttpStatus.OK);
    }

}
