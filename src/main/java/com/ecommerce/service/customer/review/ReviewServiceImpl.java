package com.ecommerce.service.customer.review;

import com.ecommerce.dto.OrderedProductDetails;
import com.ecommerce.dto.OrderedProductsResponseDto;
import com.ecommerce.dto.ReviewDto;
import com.ecommerce.entity.*;
import com.ecommerce.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final CartRepository cartRepository;

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    private final OrderRepository orderRepository;

    private final ReviewRepository reviewRepository;

    @Override
    public OrderedProductsResponseDto getOrderedProductsDetailsByOrderId(Long orderId) {
        List<CartItems> cartItems = cartRepository.findByOrderId(orderId);
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        OrderedProductsResponseDto orderedProductsResponseDto = new OrderedProductsResponseDto();
        if (optionalOrder.isPresent()) {
            Long orderAmount = optionalOrder.get().getAmount();
            orderedProductsResponseDto.setOrderAmount(orderAmount);
        }
        if (!cartItems.isEmpty()) {
            List<OrderedProductDetails> orderedProductDetailsList = new ArrayList<>();
            for (CartItems cartItem : cartItems) {
                OrderedProductDetails productDetails = new OrderedProductDetails();
                productDetails.setId(cartItem.getProduct().getId());
                productDetails.setName(cartItem.getProduct().getName());
                productDetails.setProductPrice(cartItem.getPrice());
                productDetails.setQuantity(cartItem.getQuantity());
                productDetails.setReturnedImg(cartItem.getProduct().getImg());
                orderedProductDetailsList.add(productDetails);
            }
            orderedProductsResponseDto.setOrderedProductDetailsList(orderedProductDetailsList);
        }
        return orderedProductsResponseDto;
    }

    @Override
    public ReviewDto giveReview(ReviewDto reviewDto) throws IOException {
        Optional<Product> optionalProduct = productRepository.findById(reviewDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(reviewDto.getUserId());
        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            Reviews review = new Reviews();
            review.setRating(reviewDto.getRating());
            review.setDescription(reviewDto.getDescription());
            review.setImg(reviewDto.getImg().getBytes());
            review.setUser(optionalUser.get());
            review.setProduct(optionalProduct.get());
            Reviews reviewed = reviewRepository.save(review);
            ReviewDto reviewedDto = new ReviewDto();
            reviewedDto.setId(reviewed.getId());
            return reviewedDto;
        }
        return null;
    }

}


