package com.ecommerce.service.customer.coupon;


import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.Coupon;
import com.ecommerce.entity.Order;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exceptions.ValidationException;
import com.ecommerce.repo.CouponRepository;
import com.ecommerce.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class CustomerCouponServiceImpl implements CustomerCouponService {

    private final CouponRepository couponRepository;

    private final OrderRepository orderRepository;

    public OrderDto applyCoupon(Long userId, String code) {
        Order order = orderRepository.findByUserIdAndStatus(userId, OrderStatus.Pending);
        Coupon coupon = couponRepository.findByCode(code).orElseThrow(() -> new ValidationException("Coupon not found."));

        if (couponIsExpired(coupon)) {
            throw new ValidationException("Coupon has expired.");
        }
        double discountAmount = ((coupon.getDiscount() / 100.0) * order.getTotalAmount());
        double netAmount = order.getTotalAmount() - discountAmount;

        order.setAmount((long) netAmount);
        order.setDiscount((long) discountAmount);
        order.setCoupon(coupon);


        orderRepository.save(order);
        return order.getOrderDto();
    }

    private boolean couponIsExpired(Coupon coupon) {
        Date currentDate = new Date();
        Date expirationDate = coupon.getExpirationDate();
        return expirationDate != null && currentDate.after(expirationDate);
    }
}
