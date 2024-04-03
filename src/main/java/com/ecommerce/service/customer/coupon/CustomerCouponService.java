package com.ecommerce.service.customer.coupon;

import com.ecommerce.dto.OrderDto;

public interface CustomerCouponService {

    OrderDto applyCoupon(Long userId, String code);

}
