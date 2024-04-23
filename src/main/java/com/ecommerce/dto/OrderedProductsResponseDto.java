package com.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderedProductsResponseDto {

    private List<OrderedProductDetails> orderedProductDetailsList;

    private Long orderAmount;

}
