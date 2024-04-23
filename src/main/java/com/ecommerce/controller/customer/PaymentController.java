package com.ecommerce.controller.customer;

import com.ecommerce.dto.CartItemsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.QuantityChangeProductDto;
import com.ecommerce.service.customer.cart.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import com.ecommerce.service.customer.payment.PaypalService;

import java.util.Map;


// Spring Boot (Backend)
@RestController
@RequestMapping("/api")
public class PaymentController {

    @Autowired
    private PaypalService payPalService;

    @PostMapping("/make/payment")
    public Map<String, Object> makePayment(@RequestParam("sum") String sum){
        return payPalService.createPayment(sum);
    }

    @PostMapping("/complete/payment")
    public Map<String, Object> completePayment(@RequestParam("paymentId") String paymentId, @RequestParam("payerId") String payerId){
        return payPalService.executePayment(paymentId, payerId);
    }

    @RequestMapping("/cancel")
    public String handleCancel() {
        // Handle the cancel request here
        return "Payment cancelled";
    }

    @RequestMapping("/complete/payment")
    public String handleCompletePayment() {
        // Handle the payment completion request here
        return "Payment completed";
    }
}
