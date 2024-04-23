package com.ecommerce.service.customer.customerorder;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.PlaceOrderDto;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.repo.OrderRepository;
import com.ecommerce.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerOrderServiceImpl implements CustomerOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;


    @Override
    public OrderDto PlaceOrder(PlaceOrderDto placeOrderDto) {
        Order order = orderRepository.findByUserIdAndStatus(placeOrderDto.getUserId(), OrderStatus.Pending);
        Optional<User> optionalUser = userRepository.findById(placeOrderDto.getUserId());
        if (optionalUser.isPresent()) {
            order.setOrderDescription(placeOrderDto.getOrderDescription());
            order.setStatus(OrderStatus.Placed);
            order.setDate(new Date());
            order.setTrackingId(UUID.randomUUID());
            order.setAddress(placeOrderDto.getAddress());
//            order.setAmount(order.getAmount());
            orderRepository.save(order);
            User user = optionalUser.get();
            Order newOrder = new Order();
            newOrder.setAmount(0L);
            newOrder.setTotalAmount(0L);
            newOrder.setDiscount(0L);
            newOrder.setUser(user);
            newOrder.setStatus(OrderStatus.Pending);
            orderRepository.save(newOrder);
            return order.getOrderDto();
        }
        return null;
    }

    @Override
    public List<OrderDto> getMyPlacedOrders(Long userId) {
        return orderRepository.findAllByUserIdAndStatusIn(userId, List.of(OrderStatus.Placed, OrderStatus.Shipped, OrderStatus.Delivered)).stream().map(Order::getOrderDto).collect(Collectors.toList());
    }

    @Override
    public OrderDto searchOrderByTrackingId(UUID trackingId) {
        Optional<Order> optionalOrder = orderRepository.findByTrackingId(trackingId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            return order.getOrderDto();
        }
        return null;
    }

}

