package com.ecommerce.service.customer.cart;

import com.ecommerce.dto.CartItemsDto;
import com.ecommerce.dto.OrderDto;
import com.ecommerce.dto.QuantityChangeProductDto;
import com.ecommerce.entity.CartItems;
import com.ecommerce.entity.Order;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public ResponseEntity<?> addProductToCart(CartItemsDto cartItemsDto) {
        Order pendingOrder = orderRepository.findByUserIdAndStatus(cartItemsDto.getUserId(), OrderStatus.Pending);
        Optional<CartItems> cartItem = cartRepository.findByProductIdAndOrderIdAndUserId(cartItemsDto.getProductId(), pendingOrder.getId(), cartItemsDto.getUserId());
        if (cartItem.isPresent()) {
            CartItemsDto productAlreadyExistsInCart = new CartItemsDto();
            productAlreadyExistsInCart.setProductId(null);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(productAlreadyExistsInCart);
        } else {
            Product product = null;
            Optional<Product> optionalProduct = productRepository.findById(cartItemsDto.getProductId());
            Optional<User> optionalUser = userRepository.findById(cartItemsDto.getUserId());
            Order runningOrder = orderRepository.findByUserIdAndStatus(cartItemsDto.getUserId(), OrderStatus.Pending);
            if (optionalProduct.isPresent() && optionalUser.isPresent()) {
                product = optionalProduct.get();
                CartItems cart = new CartItems();
                cart.setProduct(product);
                cart.setPrice(product.getPrice());
                cart.setQuantity(1L);
                cart.setUser(optionalUser.get());
                cart.setOrder(runningOrder);
                CartItems updatedCart = cartRepository.save(cart);
                Order order = orderRepository.findByUserAndStatus(optionalUser.get(), OrderStatus.Pending);
                order.setAmount(order.getAmount() + cart.getPrice());
                order.setTotalAmount(order.getTotalAmount() + cart.getPrice());
                order.getCartItems().add(cart);
                orderRepository.save(order);
                CartItemsDto productAddedToCartDto = new CartItemsDto();
                productAddedToCartDto.setId(updatedCart.getId());
                return ResponseEntity.status(HttpStatus.CREATED).body(productAddedToCartDto);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User or product not found");
            }
        }
    }

    @Override
    public OrderDto getCartByUserId(Long userId) {
        Order order = orderRepository.findByUserIdAndStatus(userId, OrderStatus.Pending);
        List<CartItemsDto> cartItemsDtos = order.getCartItems().stream().map(CartItems::getCartDto).collect(Collectors.toList());
        OrderDto orderDto = new OrderDto();
        orderDto.setCartItems(cartItemsDtos);
        orderDto.setAmount(order.getAmount());
        orderDto.setId(order.getId());
        orderDto.setStatus(order.getStatus());
        orderDto.setDiscount(order.getDiscount());
        if(order.getCoupon() != null){
            orderDto.setCouponName(order.getCoupon().getName());
        }

        orderDto.setTotalAmount(order.getTotalAmount());
        return orderDto;
    }

    @Override
    public OrderDto decreaseProductQuantity(QuantityChangeProductDto quantityChangeProductDto) {
        Order order = orderRepository.findByUserIdAndStatus(quantityChangeProductDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(quantityChangeProductDto.getProductId());
        Optional<CartItems> optionalCartItem = cartRepository.findByProductIdAndOrderIdAndUserId(quantityChangeProductDto.getProductId(), order.getId(), quantityChangeProductDto.getUserId());
        CartItems cartItem = optionalCartItem.get();
        order.setAmount(order.getAmount() - optionalProduct.get().getPrice());
        order.setTotalAmount(order.getTotalAmount() - optionalProduct.get().getPrice());
        cartItem.setQuantity(optionalCartItem.get().getQuantity() - 1);

        if(order.getCoupon() != null){
            double discountAmount = ((order.getCoupon().getDiscount() / 100.0) * order.getTotalAmount());
            double netAmount = order.getTotalAmount() - discountAmount;

            long discountAmountLong = (long) discountAmount;
            long netAmountLong = (long) netAmount;

            order.setAmount(netAmountLong);
            order.setDiscount(discountAmountLong);
        }
        cartRepository.save(cartItem);
        orderRepository.save(order);
        return order.getOrderDto();
    }

    @Override
    public OrderDto increaseProductQuantity(QuantityChangeProductDto quantityChangeProductDto) {
        Order order = orderRepository.findByUserIdAndStatus(quantityChangeProductDto.getUserId(), OrderStatus.Pending);
        Optional<Product> optionalProduct = productRepository.findById(quantityChangeProductDto.getProductId());
        Optional<CartItems> optionalCartItem = cartRepository.findByProductIdAndOrderIdAndUserId(quantityChangeProductDto.getProductId(), order.getId(), quantityChangeProductDto.getUserId());
        CartItems cartItem = optionalCartItem.get();
        Product product = optionalProduct.get();
        order.setAmount(order.getAmount() + optionalProduct.get().getPrice());
        order.setTotalAmount(order.getTotalAmount() + optionalProduct.get().getPrice());
        cartItem.setQuantity(optionalCartItem.get().getQuantity() + 1);

        if(order.getCoupon() != null){
            double discountAmount = ((order.getCoupon().getDiscount() / 100.0) * order.getTotalAmount());
            double netAmount = order.getTotalAmount() - discountAmount;

            long discountAmountLong = (long) discountAmount;
            long netAmountLong = (long) netAmount;

            order.setAmount(netAmountLong);
            order.setDiscount(discountAmountLong);
        }
        cartRepository.save(cartItem);
        orderRepository.save(order);
        return order.getOrderDto();
    }


}

