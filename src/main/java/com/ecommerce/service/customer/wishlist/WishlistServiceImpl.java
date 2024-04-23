package com.ecommerce.service.customer.wishlist;

import com.ecommerce.dto.WishlistDto;
import com.ecommerce.entity.Product;
import com.ecommerce.entity.User;
import com.ecommerce.entity.Wishlist;
import com.ecommerce.repo.ProductRepository;
import com.ecommerce.repo.UserRepository;
import com.ecommerce.repo.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishlistService {

    private final UserRepository userRepository;

    private final ProductRepository productRepository;

    private final WishlistRepository wishlistRepository;

    @Override
    public WishlistDto addProductToWishlist(WishlistDto wishlistDto) {
        Optional<Product> optionalProduct = productRepository.findById(wishlistDto.getProductId());
        Optional<User> optionalUser = userRepository.findById(wishlistDto.getUserId());
        if (optionalUser.isPresent() && optionalProduct.isPresent()) {
            Wishlist wishlist = new Wishlist();
            wishlist.setProduct(optionalProduct.get());
            wishlist.setUser(optionalUser.get());
            Wishlist createdWishlist = wishlistRepository.save(wishlist);
            WishlistDto createdWishlistDto = new WishlistDto();
            createdWishlistDto.setId(createdWishlist.getId());
            return createdWishlistDto;
        }
        return null;
    }

    @Override
    public List<WishlistDto> getWishlistByUserId(Long userId) {
        return wishlistRepository.findAllByUser_Id(userId).stream().map(Wishlist::getWishlistDto).collect(Collectors.toList());
    }
}
