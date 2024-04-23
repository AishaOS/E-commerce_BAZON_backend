package com.ecommerce.service.customer.review;


import com.ecommerce.dto.OrderedProductsResponseDto;
import com.ecommerce.dto.ReviewDto;

import java.io.IOException;

public interface ReviewService {


    OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId);

    ReviewDto giveReview(ReviewDto reviewDto) throws IOException;
}
