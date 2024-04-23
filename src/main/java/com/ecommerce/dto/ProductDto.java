package com.ecommerce.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto {

    private Long id;

    private String name;

    private Long price;

    private String description;

    private MultipartFile img;

    private byte[] returnedImg;

    private Long categoryId;

    private String categoryName;

}
