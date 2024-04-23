package com.ecommerce.controller;

import com.ecommerce.dto.OrderDto;
import com.ecommerce.service.customer.customerorder.CustomerOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class TrackingController {

    private final CustomerOrderService customerOrderService;

    @GetMapping("/order/{trackingId}")
    public ResponseEntity<OrderDto> searchOrderByTrackingId(@PathVariable UUID trackingId) {
        OrderDto orderDto = customerOrderService.searchOrderByTrackingId(trackingId);
        if (orderDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(orderDto);
    }

}
