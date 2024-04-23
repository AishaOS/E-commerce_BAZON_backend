package com.ecommerce.controller.customer;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.PlaceOrderDto;
import com.ecommerce.service.customer.customerorder.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerOrderController {

    private final CustomerOrderService customerOrderService;

    @PostMapping("/placeOrder")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody PlaceOrderDto placeOrderDto) {
        OrderDto OrderDto = customerOrderService.PlaceOrder(placeOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(OrderDto);
    }

    @GetMapping("/myOrders/{userId}")
    public ResponseEntity<List<OrderDto>> getMyPlacedOrders(@PathVariable Long userId) {
        List<OrderDto> orderDtos = customerOrderService.getMyPlacedOrders(userId);
        return ResponseEntity.ok(orderDtos);
    }

}
