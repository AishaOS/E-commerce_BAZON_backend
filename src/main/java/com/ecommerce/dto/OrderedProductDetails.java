package com.ecommerce.dto;

import lombok.Data;

@Data
public class OrderedProductDetails {

    private Long id;

    private String name;

    private Long productPrice;

    private Long quantity;

    private byte[] returnedImg;

}
