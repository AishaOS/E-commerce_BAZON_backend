package com.ecommerce.controller.customer;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.exceptions.ValidationException;
import com.ecommerce.service.customer.coupon.CustomerCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerCouponController {


    private final CustomerCouponService customerCouponService;

    @GetMapping("/coupon/{userId}/{code}")
    public ResponseEntity<?> applyCoupon(@PathVariable Long userId, @PathVariable String code) {
        try {
            OrderDto orderDto = customerCouponService.applyCoupon(userId,code);
            return ResponseEntity.ok(orderDto);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

}
