package com.ecommerce.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
public class AnalyticsResponse {

    private Long currentMonthOrders;
    private Long previousMonthOrders;
    private Long currentMonthEarnings;
    private Long previousMonthEarnings;

    private Long placed;
    private Long shipped;
    private Long delivered;
}
