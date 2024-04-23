package com.ecommerce.service.customer.payment;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.PlaceOrderDto;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface PaypalService {

    Map<String, Object> createPayment(String sum);
    Map<String, Object> executePayment(String paymentId, String payerId);

}
