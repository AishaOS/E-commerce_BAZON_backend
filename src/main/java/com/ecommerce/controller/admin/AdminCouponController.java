package com.ecommerce.controller.admin;

import com.ecommerce.entity.Coupon;
import com.ecommerce.exceptions.ValidationException;
import com.ecommerce.service.admin.coupon.AdminCouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/coupons")
@RequiredArgsConstructor
public class AdminCouponController {


    private final AdminCouponService adminCouponService;

    @PostMapping
    public ResponseEntity<?> createCoupon(@RequestBody Coupon coupon) {
        try {
            Coupon createdCoupon = adminCouponService.createCoupon(coupon);
            return ResponseEntity.ok(createdCoupon);
        } catch (ValidationException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Coupon>> getAllCoupons() {
        List<Coupon> coupons = adminCouponService.getAllCoupons();
        return ResponseEntity.ok(coupons);
    }

}
