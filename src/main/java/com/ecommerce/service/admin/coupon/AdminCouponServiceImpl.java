package com.ecommerce.service.admin.coupon;

import com.ecommerce.entity.Coupon;
import com.ecommerce.exceptions.ValidationException;
import com.ecommerce.repo.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminCouponServiceImpl implements AdminCouponService {

    private final CouponRepository couponRepository;

    public Coupon createCoupon(Coupon coupon) {
        // Check if the coupon code already exists in the database
        if (couponRepository.existsByCode(coupon.getCode())) {
            throw new ValidationException("Coupon code already exists.");
        }

        return couponRepository.save(coupon);
    }

    public List<Coupon> getAllCoupons() {
        return couponRepository.findAll();
    }

}
