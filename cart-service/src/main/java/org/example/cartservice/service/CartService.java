package org.example.cartservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.example.cartservice.dto.request.CartRequest;
import org.example.cartservice.dto.response.CartResponse;
import org.example.cartservice.entity.Cart;
import org.example.cartservice.entity.CartItem;
import org.example.cartservice.mapper.CartMapper;
import org.example.cartservice.repository.CartItemRepository;
import org.example.cartservice.repository.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class CartService {
    CartRepository cartRepository;
    CartMapper cartMapper;
    CartItemRepository cartItemRepository;
    public CartResponse addToCart(CartRequest cartRequest) {

        Cart cart = cartRepository.findByUserId(cartRequest.getUserId())
                .orElseGet(() -> cartRepository.save(Cart.builder()
                        .userId(cartRequest.getUserId())
                        .lastUpdated(LocalDateTime.now())
                        .build()));

        CartItem cartItem = cartMapper.toCartItem(cartRequest);
        cartItem.setCart(cart);
        cartItemRepository.save(cartItem);

        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);

        return cartMapper.toCartResponse(cart, cartItemRepository.findByCartId(cart.getId()));
    }

    public CartResponse getCartByUserId(String userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        return cartMapper.toCartResponse(cart, cartItems);
    }
    @Transactional
    public void removeFromCart(String cartId, String productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByCartIdAndProductId(cartId, productId);

        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);
    }

    public CartResponse updateCartItem(CartRequest cartRequest) {

        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartRequest.getCartId(), cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItem.setQuantity(cartRequest.getQuantity());
        cartItemRepository.save(cartItem);

        Cart cart = cartItem.getCart();
        cart.setLastUpdated(LocalDateTime.now());
        cartRepository.save(cart);

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
        return cartMapper.toCartResponse(cart, cartItems);
    }
}
