package com.ecommerce.dto;

import lombok.Data;

import java.util.List;

@Data
public class CompleteProductDetailDto {

    private ProductDto productDto;

    private List<FAQDto> faqDtoList;

    private List<ReviewDto> reviewDtoList;

}
