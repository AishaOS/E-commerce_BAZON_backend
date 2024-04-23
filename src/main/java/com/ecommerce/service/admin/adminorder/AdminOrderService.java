package com.ecommerce.service.admin.adminorder;


import com.ecommerce.dto.AnalyticsResponse;
import com.ecommerce.dto.OrderDto;

import java.util.List;

public interface AdminOrderService {

    List<OrderDto> getAllPlacedOrders();
    OrderDto changeOrderStatus(Long orderId, String status);

    AnalyticsResponse calculateAnalytics();
}
