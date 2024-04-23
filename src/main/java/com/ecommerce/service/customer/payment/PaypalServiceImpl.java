package com.ecommerce.service.customer.payment;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.paypal.api.payments.*;
import com.ecommerce.configurataion.PayPalClient.*;
import com.paypal.base.rest.PayPalRESTException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaypalServiceImpl implements PaypalService {

    @Autowired
    APIContext apiContext;

    @Override
    public Map<String, Object> createPayment(String sum) {
        Map<String, Object> response = new HashMap<>();
        Amount amount = new Amount();
        amount.setCurrency("USD");
        amount.setTotal(sum);

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl("http://localhost:8080/cancel");
        redirectUrls.setReturnUrl("http://localhost:8080/complete/payment");
        payment.setRedirectUrls(redirectUrls);

        Payment createdPayment;
        try {
            createdPayment = payment.create(apiContext);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }

    @Override
    public Map<String, Object> executePayment(String paymentId, String payerId) {
        Map<String, Object> response = new HashMap<>();
        Payment payment = new Payment();
        payment.setId(paymentId);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(payerId);
        try {
            Payment createdPayment = payment.execute(apiContext, paymentExecute);
            if(createdPayment!=null){
                response.put("status", "success");
                response.put("payment", createdPayment);
            }
        } catch (PayPalRESTException e) {
            System.err.println(e.getDetails());
        }
        return response;
    }
}

