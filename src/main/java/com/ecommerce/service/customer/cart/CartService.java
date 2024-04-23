package com.ecommerce.service.customer.cart;

import com.ecommerce.dto.CartItemsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.PlaceOrderDto;
import com.ecommerce.dto.QuantityChangeProductDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CartService {


    ResponseEntity<?> addProductToCart(CartItemsDto cartItemsDto);

    OrderDto getCartByUserId(Long userId);

    OrderDto decreaseProductQuantity(QuantityChangeProductDto quantityChangeProductDto);

    OrderDto increaseProductQuantity(QuantityChangeProductDto quantityChangeProductDto);


}
