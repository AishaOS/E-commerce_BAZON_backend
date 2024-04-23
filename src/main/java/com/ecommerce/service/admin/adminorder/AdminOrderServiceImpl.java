package com.ecommerce.service.admin.adminorder;

import com.ecommerce.dto.AnalyticsResponse;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.entity.Order;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.repo.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminOrderServiceImpl implements AdminOrderService {

    private final OrderRepository orderRepository;


    @Override
    public List<OrderDto> getAllPlacedOrders() {
        List<Order> orderList = orderRepository.findAllByStatusIn(List.of(OrderStatus.Placed,OrderStatus.Shipped,OrderStatus.Delivered));
        return orderList.stream().map(order -> {
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order.getId());
            orderDto.setOrderDescription(order.getOrderDescription());
            orderDto.setDate(order.getDate());
            orderDto.setTrackingId(order.getTrackingId());
            orderDto.setAmount(order.getAmount());
            orderDto.setPayment(order.getPayment());
            orderDto.setAddress(order.getAddress());
            orderDto.setStatus(order.getStatus());
            orderDto.setUserName(order.getUser().getName());
            return orderDto;
        }).collect(Collectors.toList());
    }

    @Override
    public OrderDto changeOrderStatus(Long orderId, String status) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Order order = optionalOrder.get();
            if (Objects.equals(status, "Shipped")) {
                order.setStatus(OrderStatus.Shipped);
            } else {
                order.setStatus(OrderStatus.Delivered);
            }
            Order order1 = orderRepository.save(order);
            OrderDto orderDto = new OrderDto();
            orderDto.setId(order1.getId());
            return orderDto;
        }
        return null;
    }

    public AnalyticsResponse calculateAnalytics() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonthDate = currentDate.minusMonths(1);

        Long currentMonthOrders = getTotalOrdersForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthOrders = getTotalOrdersForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long currentMonthEarnings = getTotalEarningsForMonth(currentDate.getMonthValue(), currentDate.getYear());
        Long previousMonthEarnings = getTotalEarningsForMonth(previousMonthDate.getMonthValue(), previousMonthDate.getYear());

        Long placed = orderRepository.countByStatus(OrderStatus.Placed);
        Long shipped = orderRepository.countByStatus(OrderStatus.Shipped);
        Long delivered = orderRepository.countByStatus(OrderStatus.Delivered);

        return new AnalyticsResponse(currentMonthOrders, previousMonthOrders, currentMonthEarnings, previousMonthEarnings, placed,
                shipped, delivered);
    }

    public Long getTotalEarningsForMonth(int month,int year) {
        // Create a Calendar instance and set it to the start of the specified year and month
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month in Calendar is zero-based
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        // Move the calendar to the end of the specified month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();

        // Fetch orders within the specified date range
        List<Order> orders = orderRepository.findByDateBetweenAndStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);

        Long sum = 0L;
        for (Order order : orders) {
            sum += order.getAmount();
        }
        return sum;
    }

    public Long getTotalOrdersForMonth(int month, int year) {
        // Assuming you have a method in your repository to fetch total orders for a given month and year
//        return orderRepository.countOrdersByMonthAndYear(month, year);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month - 1); // Month in Calendar is zero-based
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Date startOfMonth = calendar.getTime();

        // Move the calendar to the end of the specified month
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Date endOfMonth = calendar.getTime();


        // Fetch orders within the specified date range
        List<Order> orders = orderRepository.findByDateBetweenAndStatus(startOfMonth, endOfMonth, OrderStatus.Delivered);

        return (long) orders.size();
    }
}
