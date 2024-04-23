package com.ecommerce.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewDto {

    private Long id;

    private Long rating;

    private String description;

    private MultipartFile img;

    private byte[] returnedImg;

    private Long userId;

    private String username;

    private Long productId;

}
